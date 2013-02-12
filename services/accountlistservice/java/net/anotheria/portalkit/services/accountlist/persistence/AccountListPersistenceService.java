package net.anotheria.portalkit.services.accountlist.persistence;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * 
 * @author dagafonov
 * 
 */
public interface AccountListPersistenceService extends Service {

	List<AccountId> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException;

	boolean addToList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException;

	boolean removeFromList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException;

	List<AccountId> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException;

}
