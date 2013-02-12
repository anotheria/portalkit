package net.anotheria.portalkit.services.accountlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Account list representation class.
 * 
 * @author dagafonov
 * 
 */
public class AccountList {

	/**
	 * List name (like favourites, contacts, visited, visits...).
	 */
	private String listName;

	/**
	 * Involved accounts.
	 */
	private List<AccountId> targets;

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
	
	public List<AccountId> getTargets() {
		if (targets == null) {
			targets = Collections.synchronizedList(new ArrayList<AccountId>());
		}
		return targets;
	}
	
	public void addAll(Collection<AccountId> targets) {
		getTargets().addAll(targets);
	}
	
	public void removeAll(Collection<AccountId> targets) {
		getTargets().removeAll(targets);
	}

	public void addTarget(AccountId target) {
		getTargets().add(target);
	}

	public void removeTarget(AccountId target) {
		getTargets().remove(target);
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

	

}
