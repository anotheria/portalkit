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
 * @todo : curent implementation does not provides update functionality... It's
 *       important for ( visitS - last visit time update for example) Seems we
 *       need to remove create, and add save method!
 * 
 * @todo : last changes were done - to prevent Unique key violation errors -
 *       which was possible .. Added as Temp solution! - but when SAVE will be
 *       provided - last changes should be simply rolled back!
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
	private Cache<AccountId, AccountListData> accountListsCache;

	private Cache<AccountId, AccountListData> reverseAccountListsCache;

	private AccountListServiceAnnouncer announcer;

	/**
	 * Constructor.
	 */
	public AccountListServiceImpl() {
		accountListsCache = Caches.createHardwiredCache("accountlistservice-cacheaccountlists");
		reverseAccountListsCache = Caches.createHardwiredCache("reverseaccountlistservice-cacheaccountlists");
		try {
			persistenceService = MetaFactory.get(AccountListPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}
		announcer = new AccountListServiceAnnouncer();
	}

	@Override
	public boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException {
		return updateInList(owner, listName, targets);
	}

	@Override
	public boolean updateInList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListServiceException {
		IdBasedLock<AccountId> lock = lockManager.obtainLock(owner);
		lock.lock();
		try {

			for (AccountIdAdditionalInfo itemToCreate : targets) {
				if (itemToCreate.getCreationTimestamp() == 0)
					itemToCreate.setCreationTimestamp(System.currentTimeMillis());
			}

			Set<AccountIdAdditionalInfo> existing = new HashSet<AccountIdAdditionalInfo>(getListInternally(owner, listName));
			List<AccountIdAdditionalInfo> itemsToCreate = getListToCreate(existing, targets);
			List<AccountIdAdditionalInfo> itemsToUpdate = getListToUpdate(existing, targets);

			boolean resUpdate = persistenceService.updateInList(owner, listName, itemsToUpdate);
			if (resUpdate) {
				updateAddCaches(owner, listName, itemsToUpdate);
				announceUpdate(owner, listName, itemsToUpdate);
			}
			boolean resCreate = persistenceService.addToList(owner, listName, itemsToCreate);
			if (resCreate) {
				updateAddCaches(owner, listName, itemsToCreate);
				announceCreate(owner, listName, itemsToCreate);
			}
			return resUpdate && resCreate;
		} catch (AccountListPersistenceServiceException e) {
			final String message = LogMessageUtil.failMsg(e, owner, listName, targets.size());
			LOGGER.error(message, e);
			throw new AccountListServiceException(message, e);
		} finally {
			lock.unlock();
		}
	}

	private void updateAddCaches(AccountId owner, String listName, List<AccountIdAdditionalInfo> targets) {
		if (targets == null || targets.size() == 0) {
			return;
		}
		AccountListData fromCache = accountListsCache.get(owner);
		if (fromCache == null) {
			fromCache = new AccountListData(owner);
			accountListsCache.put(owner, fromCache);
		}
		fromCache.addAll(listName, targets);
		addReverseStorage(owner, listName, targets);
	}

	private void updateRemoveCaches(AccountId owner, String listName, List<AccountIdAdditionalInfo> targets) {
		if (targets == null || targets.size() == 0) {
			return;
		}
		AccountListData fromCache = accountListsCache.get(owner);
		if (fromCache != null) {
			fromCache.removeAll(listName, targets);
		}
		removeReverseStorage(owner, listName, targets);
	}

	private void addReverseStorage(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) {
		for (AccountIdAdditionalInfo target : targets) {
			AccountListData accListData = reverseAccountListsCache.get(target.getAccountId());
			if (accListData != null) {
				accListData.addAll(listName, Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(owner, target
						.getAdditionalInfo(), target.getCreationTimestamp()) }));
			} else {
				accListData = new AccountListData(target.getAccountId(), listName,
						Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(owner, target.getAdditionalInfo(), target
								.getCreationTimestamp()) }));
				reverseAccountListsCache.put(target.getAccountId(), accListData);
			}
		}
	}

	private void removeReverseStorage(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) {
		for (AccountIdAdditionalInfo target : targets) {
			AccountListData accListData = reverseAccountListsCache.get(target.getAccountId());
			if (accListData != null) {
				accListData.removeAll(listName,
						Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(owner, target.getAdditionalInfo()) }));
			}
		}
	}

	private List<AccountIdAdditionalInfo> getListToUpdate(Set<AccountIdAdditionalInfo> existing, Collection<AccountIdAdditionalInfo> targets) {
		List<AccountIdAdditionalInfo> res = new ArrayList<AccountIdAdditionalInfo>();
		for (AccountIdAdditionalInfo t : targets) {
			if (existing.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}

	private List<AccountIdAdditionalInfo> getListToCreate(Set<AccountIdAdditionalInfo> existing, Collection<AccountIdAdditionalInfo> targets) {
		List<AccountIdAdditionalInfo> res = new ArrayList<AccountIdAdditionalInfo>();
		for (AccountIdAdditionalInfo t : targets) {
			if (!existing.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}

	@Override
	public boolean addToList(AccountId owner, String listName, AccountIdAdditionalInfo firstTarget, AccountIdAdditionalInfo... moreTargets)
			throws AccountListServiceException {
		List<AccountIdAdditionalInfo> targets = new ArrayList<AccountIdAdditionalInfo>();
		if (firstTarget != null) {
			targets.add(firstTarget);
		}
		if (moreTargets != null && moreTargets.length > 0) {
			targets.addAll(Arrays.asList(moreTargets));
		}
		return addToList(owner, listName, targets);
	}

	@Override
	public List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListServiceException {
		AccountListData fromCache = accountListsCache.get(owner);
		if (fromCache != null) {
			AccountList accList = fromCache.getLists().get(listName);
			if (accList != null && !accList.getTargets().isEmpty()) {
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
		if (firstTarget != null) {
			targets.add(firstTarget);
		}
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
			// reading current entries
			Set<AccountIdAdditionalInfo> existing = new HashSet<AccountIdAdditionalInfo>(getListInternally(owner, listName));
			List<AccountIdAdditionalInfo> itemsToRemove = new ArrayList<AccountIdAdditionalInfo>(targets);

			// clearing deletion list, skip only existing items
			for (AccountIdAdditionalInfo itemToRemove : targets) {
				// to prevent Duplicate entries!
				if (!existing.contains(itemToRemove)) {
					addDebugMessage(itemToRemove, false);
					continue;
				}
				itemsToRemove.add(itemToRemove);
			}

			boolean res = persistenceService.removeFromList(owner, listName, itemsToRemove);
			if (res) {
				updateRemoveCaches(owner, listName, itemsToRemove);
				announceDelete(owner, listName, itemsToRemove);
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
		// todo : incoming parameters validation ???
		AccountListData fromCache = reverseAccountListsCache.get(target);
		if (fromCache != null) {
			AccountList accList = fromCache.getLists().get(listName);
			if (accList != null && !accList.getTargets().isEmpty()) {
				return accList.getTargets();
			}
		}
		try {
			List<AccountIdAdditionalInfo> res = persistenceService.getReverseList(target, listName);
			if (res == null) {
				res = new ArrayList<AccountIdAdditionalInfo>();
			}
			AccountListData ald = new AccountListData(target, listName, res);
			reverseAccountListsCache.put(target, ald);
			return ald.getLists().get(listName).getTargets();
		} catch (AccountListPersistenceServiceException e) {
			final String message = LogMessageUtil.failMsg(e, target, listName);
			LOGGER.error(message, e);
			throw new AccountListServiceException(message, e);
		}
	}

	/**
	 * Perform data read - without locking, method created for internal usage,
	 * and does not require locking.
	 * 
	 * @param owner
	 *            {@link AccountId} list owner
	 * @param listName
	 *            list id
	 * @return {@link AccountIdAdditionalInfo} collection
	 * @throws AccountListServiceException
	 *             on errors
	 */
	private List<AccountIdAdditionalInfo> getListInternally(final AccountId owner, final String listName) throws AccountListServiceException {
		AccountListData fromCache = accountListsCache.get(owner);
		if (fromCache != null) {
			AccountList accList = fromCache.getLists().get(listName);
			if (accList != null && accList.getTargets() != null && !accList.getTargets().isEmpty())
				return accList.getTargets();
		}
		try {
			List<AccountIdAdditionalInfo> fromPersistence = persistenceService.getList(owner, listName);
			AccountListData accListData = new AccountListData(owner, listName, fromPersistence);
			accountListsCache.put(owner, accListData);
			return fromPersistence;
		} catch (AccountListPersistenceServiceException e) {
			final String message = LogMessageUtil.failMsg(e, owner, listName);
			LOGGER.error(message, e);
			throw new AccountListServiceException(message, e);
		}
	}

	/**
	 * Add debug message. Actually writes to debug log - information - why some
	 * item was skipped from add/remove stage.
	 * 
	 * @param itemToCreate
	 *            {@link AccountIdAdditionalInfo}
	 * @param isCreateCall
	 *            {@code true} if call comes from create method, {@code false}
	 *            otherwise
	 */
	private void addDebugMessage(AccountIdAdditionalInfo itemToCreate, boolean isCreateCall) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(isCreateCall ? "Skipping " + itemToCreate + " creation! Cause it's  exist" : "Skipping " + itemToCreate
					+ " deletion! Cause it's  does not exists");
	}

	private void announceCreate(AccountId owner, String listName, List<AccountIdAdditionalInfo> instances) {
		for (AccountIdAdditionalInfo info : instances) {
			announcer.accountListCreate(owner, info.getAccountId(), listName);
		}
	}

	private void announceUpdate(AccountId owner, String listName, List<AccountIdAdditionalInfo> instances) {
		for (AccountIdAdditionalInfo info : instances) {
			announcer.accountListUpdate(owner, info.getAccountId(), listName);
		}
	}

	private void announceDelete(AccountId owner, String listName, List<AccountIdAdditionalInfo> instances) {
		for (AccountIdAdditionalInfo info : instances) {
			announcer.accountListDelete(owner, info.getAccountId(), listName);
		}
	}

}
