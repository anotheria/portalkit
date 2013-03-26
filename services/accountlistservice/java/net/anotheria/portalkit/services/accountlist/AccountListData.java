package net.anotheria.portalkit.services.accountlist;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Represents account with list of linked accounts through the lists like
 * categories.
 * 
 * @author dagafonov
 * 
 */
public class AccountListData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -206790452804778000L;

	/**
	 * Account id of the user.
	 */
	private AccountId accountId;

	/**
	 * Hash map of lists.
	 */
	private Map<String, AccountList> lists;

	/**
	 * Constructs object just with accountId value.
	 * 
	 * @param owner
	 */
	public AccountListData(AccountId owner) {
		this.accountId = owner;
	}

	/**
	 * Constructs object with all information.
	 * 
	 * @param owner
	 * @param listName
	 * @param targets
	 */
	public AccountListData(AccountId owner, String listName, Collection<AccountIdAdditionalInfo> targets) {
		this.accountId = owner;
		AccountList accL = new AccountList(listName);
		accL.addAll(targets);
		getLists().put(accL.getListName(), accL);
	}

	/**
	 * Get account id.
	 * @return
	 */
	public AccountId getAccountId() {
		return accountId;
	}

	public Map<String, AccountList> getLists() {
		if (lists == null) {
			lists = new ConcurrentHashMap<String, AccountList>();
		}
		return lists;
	}

	public void addAll(String listName, Collection<AccountIdAdditionalInfo> targets) {
		AccountList accList = getLists().get(listName);
		Set<AccountIdAdditionalInfo> unique = new HashSet<AccountIdAdditionalInfo>();
		if (accList == null) {
			accList = new AccountList(listName);
			unique.addAll(targets);
			accList.addAll(unique);
			getLists().put(accList.getListName(), accList);
		} else {
			unique.addAll(accList.getTargets());
			unique.addAll(targets);
			accList.addAll(unique);
		}
	}

	public void removeAll(String listName, Collection<AccountIdAdditionalInfo> targets) {
		AccountList accList = getLists().get(listName);
		if (accList != null) {
			accList.removeAll(targets);
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

	@Override
	public String toString() {
		return "AccountListData [accountId=" + accountId + ", lists=" + lists + "]";
	}

}
