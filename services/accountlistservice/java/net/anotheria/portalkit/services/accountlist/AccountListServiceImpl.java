package net.anotheria.portalkit.services.accountlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

/**
 * AccountList service implementation.
 * 
 * @author dagafonov
 * 
 */
public class AccountListServiceImpl implements AccountListService {

	private AccountListPersistenceService persistenceService;

	private IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<AccountId>();

	private Cache<AccountId, AccountListData> accountLists;

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
		try {
			lock.lock();
			boolean res = false;
			for (AccountIdAdditionalInfo t : targets) {
				t.setCreationTimestamp(System.currentTimeMillis());
			}
			res = persistenceService.addToList(owner, listName, targets);
			if (res) {
				AccountListData results = accountLists.get(owner);
				if (results == null) {
					results = new AccountListData(owner);
					accountLists.put(owner, results);
				}
				results.addAll(listName, targets);
			}
			return res;
		} catch (AccountListPersistenceServiceException e) {
			throw new AccountListServiceException("persistenceService.addToList failed", e);
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
		try {
			lock.lock();
			List<AccountIdAdditionalInfo> fromPersistence = persistenceService.getList(owner, listName);
			AccountListData accListData = new AccountListData(owner, listName, fromPersistence);
			accountLists.put(owner, accListData);
			return fromPersistence;
		} catch (AccountListPersistenceServiceException e) {
			throw new AccountListServiceException("persistenceService.getList() failed", e);
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
		try {
			lock.lock();
			boolean res = false;
			res = persistenceService.removeFromList(owner, listName, targets);
			if (res) {
				AccountListData results = accountLists.get(owner);
				if (results != null) {
					AccountList accList = results.getLists().get(listName);
					if (accList != null) {
						accList.removeAll(targets);
						res = true;
					}
				}
			}
			return res;
		} catch (AccountListPersistenceServiceException e) {
			throw new AccountListServiceException("persistenceService.removeFromList failed", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public List<AccountIdAdditionalInfo> reverseLookup(AccountId target, String listName) throws AccountListServiceException {
		List<AccountIdAdditionalInfo> res = null;
		try {
			res = persistenceService.getReverseList(target, listName);
			if (res == null) {
				res = new ArrayList<AccountIdAdditionalInfo>();
			}
			return res;
		} catch (AccountListPersistenceServiceException e) {
			throw new AccountListServiceException("persistenceService.getReverseList() failed", e);
		}
	}

}
