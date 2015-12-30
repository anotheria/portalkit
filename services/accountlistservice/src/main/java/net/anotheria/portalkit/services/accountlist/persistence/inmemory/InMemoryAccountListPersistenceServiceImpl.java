package net.anotheria.portalkit.services.accountlist.persistence.inmemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.portalkit.services.accountlist.AccountIdAdditionalInfo;
import net.anotheria.portalkit.services.accountlist.AccountList;
import net.anotheria.portalkit.services.accountlist.AccountListData;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceService;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * In-memory implementation of AccountListPersistence service.
 * 
 * @author dagafonov
 * 
 */
public class InMemoryAccountListPersistenceServiceImpl implements AccountListPersistenceService {

	/**
	 * Storage hash map.
	 */
	private ConcurrentHashMap<AccountId, AccountListData> storage = new ConcurrentHashMap<AccountId, AccountListData>();
	
	/**
	 * Reverse storage hash map.
	 */
	private ConcurrentHashMap<AccountId, AccountListData> reverseStorage = new ConcurrentHashMap<AccountId, AccountListData>();

	@Override
	public boolean addToList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListPersistenceServiceException {
		return updateInList(owner, listName, targets);
	}
	
	@Override
	public boolean updateInList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets)
			throws AccountListPersistenceServiceException {
		AccountListData accListData = storage.get(owner);
		if (accListData == null) {
			accListData = new AccountListData(owner, listName, targets);
			storage.put(owner, accListData);
		} else {
			accListData.addAll(listName, targets);
		}
		addReverseStorage(owner, listName, targets);
		return true;
	}

	@Override
	public List<AccountIdAdditionalInfo> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException {
		List<AccountIdAdditionalInfo> accIdList = new ArrayList<AccountIdAdditionalInfo>();
		AccountListData accListData = storage.get(owner);
		if (accListData != null) {
			AccountList accList = accListData.getLists().get(listName);
			if (accList != null) {
				accIdList.addAll(accList.getTargets());
			}
		}
		return accIdList;
	}

	@Override
	public boolean removeFromList(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) throws AccountListPersistenceServiceException {
		AccountListData accListData = storage.get(owner);
		if (accListData != null) {
			AccountList accList = accListData.getLists().get(listName);
			if (accList != null) {
				accList.removeAll(targets);
			}
		}
		removeReverseStorage(owner, listName, targets);
		return true;
	}

	private void addReverseStorage(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) {
		for (AccountIdAdditionalInfo target : targets) {
			AccountListData accListData = reverseStorage.get(target.getAccountId());
			if (accListData != null) {
				accListData.addAll(listName, Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(owner, target.getAdditionalInfo(), target.getCreationTimestamp()) }));
			} else {
				accListData = new AccountListData(target.getAccountId(), listName, Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(owner, target.getAdditionalInfo(), target.getCreationTimestamp()) }));
				reverseStorage.put(target.getAccountId(), accListData);
			}
		}
	}

	private void removeReverseStorage(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) {
		for (AccountIdAdditionalInfo target : targets) {
			AccountListData accListData = reverseStorage.get(target.getAccountId());
			if (accListData != null) {
				accListData.removeAll(listName, Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(owner, target.getAdditionalInfo()) }));
			}
		}
	}

	@Override
	public List<AccountIdAdditionalInfo> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException {
		List<AccountIdAdditionalInfo> accIdList = new ArrayList<AccountIdAdditionalInfo>();
		AccountListData accListData = reverseStorage.get(target);
		if (accListData != null) {
			AccountList accList = accListData.getLists().get(listName);
			if (accList != null) {
				accIdList.addAll(accList.getTargets());
			}
		}
		return accIdList;
	}

}
