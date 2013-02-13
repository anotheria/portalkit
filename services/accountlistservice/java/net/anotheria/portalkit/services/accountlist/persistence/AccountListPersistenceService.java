package net.anotheria.portalkit.services.accountlist.persistence;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Account list persistence service interface.
 * 
 * @author dagafonov
 * 
 */
public interface AccountListPersistenceService extends Service {

	/**
	 * Gets list of involved accounts into specified list of owner account.
	 * 
	 * @param owner
	 * @param listName
	 * @return
	 * @throws AccountListPersistenceServiceException
	 */
	List<AccountId> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException;

	/**
	 * Adds list of involved accounts into specified list of owner account.
	 * 
	 * @param owner
	 * @param listName
	 * @param targets
	 * @return
	 * @throws AccountListPersistenceServiceException
	 */
	boolean addToList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException;

	/**
	 * Removes list of involved accounts into specified list of owner account.
	 * 
	 * @param owner
	 * @param listName
	 * @param targets
	 * @return
	 * @throws AccountListPersistenceServiceException
	 */
	boolean removeFromList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException;

	/**
	 * Gets list of owners that have list where target account is involved.
	 * 
	 * @param target
	 * @param listName
	 * @return
	 * @throws AccountListPersistenceServiceException
	 */
	List<AccountId> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException;

}
