package net.anotheria.portalkit.services.accountlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Represents list of accounts for specified account list.
 * 
 * @author dagafonov
 * 
 */
public class AccountList {

	/**
	 * Name of the list (like favourites, contacts, visited, visits...).
	 */
	private String listName;

	/**
	 * Involved accounts.
	 */
	private List<AccountIdAdditionalInfo> targets;
	
	public AccountList(String listName) {
		this.listName = listName;
	}

	public String getListName() {
		return listName;
	}

	public List<AccountIdAdditionalInfo> getTargets() {
		if (targets == null) {
			targets = Collections.synchronizedList(new ArrayList<AccountIdAdditionalInfo>());
		}
		return targets;
	}

	/**
	 * Adds values to the targets list. Only new account ids will be added.
	 * 
	 * @param targets
	 */
	public void addAll(Collection<AccountIdAdditionalInfo> targets) {
		HashSet<AccountIdAdditionalInfo> set = new HashSet<AccountIdAdditionalInfo>(getTargets());
		getTargets().clear();
		
		set.addAll(targets);
		getTargets().addAll(set);
	}

	public void removeAll(Collection<AccountIdAdditionalInfo> targets) {
		getTargets().removeAll(targets);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listName == null) ? 0 : listName.hashCode());
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
		AccountList other = (AccountList) obj;
		if (listName == null) {
			if (other.listName != null)
				return false;
		} else if (!listName.equals(other.listName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountList [listName=" + listName + ", targets=" + targets + "]";
	}

}
