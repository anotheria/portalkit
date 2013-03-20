package net.anotheria.portalkit.services.accountlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceService;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import net.anotheria.util.log.LogMessageUtil;
import org.apache.log4j.Logger;

/**
 * AccountList service implementation.
 *
 * @author dagafonov
 *
 * //TODO :  curent implementation does not provides update functionality...  It's  important for ( visitS - last  visit time update  for  example) Seems  we need to remove  create, and  add save  method!
 * //TODO : last  changes  were done - to prevent Unique key violation errors - which was possible .. Added  as Temp  solution!  - but  when SAVE  will be provided - last  changes should be simply rolled back!
 */
public class AccountListServiceImpl implements AccountListService {
    /**
     * Logging utility instance.
     */
    private static final Logger LOGGER = Logger.getLogger(AccountListServiceImpl.class);
    /**
     * {@link AccountListPersistenceService} instance.
     */
    private AccountListPersistenceService persistenceService;
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<AccountId>();
    /**
     * Data cache. Maps {@link AccountId} to {@link AccountListData}.
     */
    private Cache<AccountId, AccountListData> accountLists;

    /**
     * Constructor.
     */
    public AccountListServiceImpl() {
        accountLists = Caches.createHardwiredCache("accountlistservice-cacheaccountlists");
        try {
            persistenceService = MetaFactory.get(AccountListPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't start without persistence service ", e);
        }
    }

    @Override
    public boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(owner);
        lock.lock();
        try {
            //reading current entries
            Set<AccountIdAdditionalInfo> contacts = new HashSet<AccountIdAdditionalInfo>(getListInternally(owner, listName));
            List<AccountIdAdditionalInfo> itemsToCreate = new ArrayList<AccountIdAdditionalInfo>();

            for (AccountIdAdditionalInfo itemToCreate : targets) {
                //to prevent Duplicate entries!
                if (contacts.contains(itemToCreate)) {
                    addDebugMessage(itemToCreate, true);
                    continue;
                }
                itemsToCreate.add(itemToCreate);
                // just provides possibility to control property value from top lvl abstraction...
                if (itemToCreate.getCreationTimestamp() == 0)
                    itemToCreate.setCreationTimestamp(System.currentTimeMillis());
            }

            final boolean res = persistenceService.addToList(owner, listName, itemsToCreate);
            if (res) {
                AccountListData results = accountLists.get(owner);
                if (results == null) {
                    results = new AccountListData(owner);
                    accountLists.put(owner, results);
                }
                results.addAll(listName, itemsToCreate);
            }
            return res;
        } catch (AccountListPersistenceServiceException e) {
            final String message = LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListServiceException(message, e);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public boolean addToList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTargets) throws AccountListServiceException {
        List<AccountIdAdditionalInfo> targets = new ArrayList<AccountIdAdditionalInfo>();
        targets.add(firstTarget);
        if (moreTargets != null && moreTargets.length > 0) {
            targets.addAll(Arrays.asList(moreTargets));
        }
        return addToList(owner, listName, targets);
    }

    @Override
    public List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListServiceException {
        AccountListData fromCache = accountLists.get(owner);
        if (fromCache != null) {
            AccountList accList = fromCache.getLists().get(listName);
            if (accList != null && accList.getTargets().size() > 0) {
                return accList.getTargets();
            }
        }
        IdBasedLock<AccountId> lock = lockManager.obtainLock(owner);
        lock.lock();
        try {
            return getListInternally(owner, listName);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public boolean removeFromList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTargets)
            throws AccountListServiceException {
        List<AccountIdAdditionalInfo> targets = new ArrayList<AccountIdAdditionalInfo>();
        targets.add(firstTarget);
        if (moreTargets != null && moreTargets.length > 0) {
            targets.addAll(Arrays.asList(moreTargets));
        }
        return removeFromList(owner, listName, targets);
    }

    @Override
    public boolean removeFromList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(owner);
        lock.lock();
        try {
            //reading current entries
            Set<AccountIdAdditionalInfo> contacts = new HashSet<AccountIdAdditionalInfo>(getListInternally(owner, listName));
            List<AccountIdAdditionalInfo> itemsToRemove = new ArrayList<AccountIdAdditionalInfo>();

            for (AccountIdAdditionalInfo itemToRemove : targets) {
                //to prevent Duplicate entries!
                if (!contacts.contains(itemToRemove)) {
                    addDebugMessage(itemToRemove, false);
                    continue;
                }
                itemsToRemove.add(itemToRemove);

            }


            boolean res = persistenceService.removeFromList(owner, listName, itemsToRemove);
            if (res) {
                AccountListData results = accountLists.get(owner);
                if (results != null) {
                    AccountList accList = results.getLists().get(listName);
                    if (accList != null) {
                        accList.removeAll(itemsToRemove);
                        res = true;
                    }
                }
            }
            return res;
        } catch (AccountListPersistenceServiceException e) {
            final String message = LogMessageUtil.failMsg(e, owner, listName, targets.size());
            LOGGER.error(message, e);
            throw new AccountListServiceException(message, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<AccountIdAdditionalInfo> reverseLookup(AccountId target, String listName) throws AccountListServiceException {
        //  todo  : incoming parameters  validation ???
        try {
            List<AccountIdAdditionalInfo> res = persistenceService.getReverseList(target, listName);
            return (res == null) ? new ArrayList<AccountIdAdditionalInfo>() : res;
        } catch (AccountListPersistenceServiceException e) {
            final String message = LogMessageUtil.failMsg(e, target, listName);
            LOGGER.error(message, e);
            throw new AccountListServiceException(message, e);
        }
    }

    /**
     * Perform data read - without locking, method created for internal usage, and does not require locking.
     *
     * @param owner    {@link AccountId} list owner
     * @param listName list id
     * @return {@link AccountIdAdditionalInfo} collection
     * @throws AccountListServiceException on errors
     */
    private List<AccountIdAdditionalInfo> getListInternally(final AccountId owner, final String listName) throws AccountListServiceException {
        AccountListData fromCache = accountLists.get(owner);
        if (fromCache != null) {
            AccountList accList = fromCache.getLists().get(listName);
            if (accList != null && accList.getTargets() != null && !accList.getTargets().isEmpty())
                return accList.getTargets();
        }
        try {
            List<AccountIdAdditionalInfo> fromPersistence = persistenceService.getList(owner, listName);
            AccountListData accListData = new AccountListData(owner, listName, fromPersistence);
            accountLists.put(owner, accListData);
            return fromPersistence;
        } catch (AccountListPersistenceServiceException e) {
            final String message = LogMessageUtil.failMsg(e, owner, listName);
            LOGGER.error(message, e);
            throw new AccountListServiceException(message, e);
        }
    }

    /**
     * Add debug message.
     * Actually writes to debug log - information - why some item was skipped from add/remove stage.
     *
     * @param itemToCreate {@link AccountIdAdditionalInfo}
     * @param isCreateCall {@code true} if call comes from create method, {@code false} otherwise
     */
    private void addDebugMessage(AccountIdAdditionalInfo itemToCreate, boolean isCreateCall) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(isCreateCall ? "Skipping " + itemToCreate + " creation! Cause it's  exist" : "Skipping " + itemToCreate + " deletion! Cause it's  does not exists");
    }

}
