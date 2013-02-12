package net.anotheria.portalkit.services.accountlist;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Represent of account with list of accounts.
 * 
 * @author dagafonov
 * 
 */
public class AccountListData {

	private AccountId accountId;

	private Map<String, AccountList> lists;

	public AccountListData(AccountId owner) {
		this.accountId = owner;
	}
	
	public AccountListData(AccountId owner, String listName, Collection<AccountId>  targets) {
		this.accountId = owner;
		AccountList accL = new AccountList();
		accL.addAll(targets);
		getLists().put(listName, accL);
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	public Map<String, AccountList> getLists() {
		if (lists == null) {
			lists = new ConcurrentHashMap<String, AccountList>();
		}
		return lists;
	}

	public void setLists(Map<String, AccountList> lists) {
		this.lists = lists;
	}

	public void addAll(String listName, Collection<AccountId> targets) {
		AccountList accList = getLists().get(listName);
		if (accList == null) {
			accList = new AccountList();
			accList.addAll(targets);
			getLists().put(listName, accList);
		} else {
			accList.addAll(targets);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountListData other = (AccountListData) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		return true;
	}

}
