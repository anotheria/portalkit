package net.anotheria.portalkit.services.accountlist.persistence.inmemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

	private ConcurrentHashMap<AccountId, AccountListData> storage = new ConcurrentHashMap<AccountId, AccountListData>();
	private ConcurrentHashMap<AccountId, AccountListData> reverseStorage = new ConcurrentHashMap<AccountId, AccountListData>();

	@Override
	public boolean addToList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException {
		AccountListData accListData = storage.get(owner);
		if (accListData == null) {
			accListData = new AccountListData(owner, listName, targets);
			storage.put(owner, accListData);
		} else {
			AccountList accList = accListData.getLists().get(listName);
			if (accList == null) {
				accList = new AccountList(listName);
				accList.addAll(targets);
				accListData.getLists().put(listName, accList);
			} else {
				accList.addAll(targets);
			}
		}
		addReverseStorage(owner, listName, targets);
		return true;
	}

	@Override
	public List<AccountId> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException {
		List<AccountId> accIdList = new ArrayList<AccountId>();
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
	public boolean removeFromList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException {
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

	private void addReverseStorage(AccountId owner, String listName, Collection<AccountId> targets) {
		for (AccountId target : targets) {
			AccountListData accListData = reverseStorage.get(target);
			if (accListData != null) {
				accListData.addAll(listName, Arrays.asList(new AccountId[] { owner }));
			} else {
				accListData = new AccountListData(target, listName, Arrays.asList(new AccountId[] { owner }));
				reverseStorage.put(target, accListData);
			}
		}
	}

	private void removeReverseStorage(AccountId owner, String listName, Collection<AccountId> targets) {
		for (AccountId target : targets) {
			AccountListData accListData = reverseStorage.get(target);
			if (accListData != null) {
				accListData.removeAll(listName, Arrays.asList(new AccountId[] { owner }));
			}
		}
	}

	@Override
	public List<AccountId> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException {
		List<AccountId> accIdList = new ArrayList<AccountId>();
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
