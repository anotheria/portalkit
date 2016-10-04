package net.anotheria.portalkit.services.account.persistence;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Interface for the persistence service for the account service.
 * 
 * @author lrosenberg
 * @since 12.12.12 11:41
 */
public interface AccountPersistenceService extends Service {

	/**
	 * Returns the account with that account id.
	 * 
	 * @param id account id.
	 * @return Account
	 * @throws AccountPersistenceServiceException if error.
	 */
	Account getAccount(AccountId id) throws AccountPersistenceServiceException;

	/**
	 * Saves the account.
	 * 
	 * @param account	{@link	Account}
	 * @throws AccountPersistenceServiceException if error.
	 */
	void saveAccount(Account account) throws AccountPersistenceServiceException;

	/**
	 * Deletes the account with submitted id.
	 * 
	 * @param id account id
	 * @throws AccountPersistenceServiceException if error.
	 */
	void deleteAccount(AccountId id) throws AccountPersistenceServiceException;

	/**
	 * Returns the id of the account with the given name.
	 * 
	 * @param name	user name.
	 * @return AccountId
	 * @throws AccountPersistenceServiceException if error.
	 */
	AccountId getIdByName(String name) throws AccountPersistenceServiceException;

	/**
	 * Returns the id of the account with the given email.
	 * 
	 * @param email
	 * @return AccountId
	 * @throws AccountPersistenceServiceException if error.
	 */
	AccountId getIdByEmail(String email) throws AccountPersistenceServiceException;

	/**
	 * Get all account id's.
	 * 
	 * @return {@link Collection} of {@link AccountId}
	 * @throws AccountPersistenceServiceException if error.
	 */
	Collection<AccountId> getAllAccountIds() throws AccountPersistenceServiceException;

	/**
	 * 
	 * @param id account id
	 * @return list of {@link AccountId}.
	 * @throws AccountPersistenceServiceException if error.
	 */
	List<AccountId> getAccountsByType(int id) throws AccountPersistenceServiceException;
	
	/**
	 * Get accounts by query.
	 * 
	 * @param query
	 *            {@link AccountQuery}
	 * @return {@link List} of {@link Account}
	 * @throws AccountPersistenceServiceException if error.
	 */
	List<Account> getAccountsByQuery(AccountQuery query) throws AccountPersistenceServiceException;

}
