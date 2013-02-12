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

	// cache of the account lists for each owner where stored targets
	private Cache<AccountId, AccountListData> accountLists;

//	// cache of users where they are as targets in list of other users
//	private Cache<String, List<AccountId>> reverseAccountLists;

	public AccountListServiceImpl() {

		accountLists = Caches.createHardwiredCache("accountlistservice-cacheaccountlists");
//		reverseAccountLists = Caches.createHardwiredCache("accountlistservice-cachereverseaccountlists");

		try {
			persistenceService = MetaFactory.get(AccountListPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}
	}

	@Override
	public boolean addToList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListServiceException {
		IdBasedLock<AccountId> lock = lockManager.obtainLock(owner);
		try {
			lock.lock();
			boolean res = false;
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
	public boolean addToList(AccountId owner, String listName, AccountId firstTarget, AccountId... moreTargets) throws AccountListServiceException {
		List<AccountId> targets = new ArrayList<AccountId>();
		targets.add(firstTarget);
		if (moreTargets != null && moreTargets.length > 0) {
			targets.addAll(Arrays.asList(moreTargets));
		}
		return addToList(owner, listName, targets);
	}

	@Override
	public List<AccountId> getList(AccountId owner, String listName) throws AccountListServiceException {
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
			List<AccountId> fromPersistence = persistenceService.getList(owner, listName);
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
	public boolean removeFromList(AccountId owner, String listName, AccountId firstTarget, AccountId... moreTargets)
			throws AccountListServiceException {
		List<AccountId> targets = new ArrayList<AccountId>();
		targets.add(firstTarget);
		if (moreTargets != null && moreTargets.length > 0) {
			targets.addAll(Arrays.asList(moreTargets));
		}
		return removeFromList(owner, listName, targets);
	}

	@Override
	public boolean removeFromList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListServiceException {
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
	public List<AccountId> reverseLookup(AccountId target, String listName) throws AccountListServiceException {
		List<AccountId> res = null;
		try {
			res = persistenceService.getReverseList(target, listName);
			if (res == null) {
				res = new ArrayList<AccountId>();
			}
			return res;
		} catch (AccountListPersistenceServiceException e) {
			throw new AccountListServiceException("persistenceService.getReverseList() failed", e);
		}
	}

}
