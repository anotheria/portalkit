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
	 * @param id
	 * @return Account
	 * @throws AccountPersistenceServiceException
	 */
	Account getAccount(AccountId id) throws AccountPersistenceServiceException;

	/**
	 * Saves the account.
	 * 
	 * @param account
	 * @throws AccountPersistenceServiceException
	 */
	void saveAccount(Account account) throws AccountPersistenceServiceException;

	/**
	 * Deletes the account with submitted id.
	 * 
	 * @param id
	 * @throws AccountPersistenceServiceException
	 */
	void deleteAccount(AccountId id) throws AccountPersistenceServiceException;

	/**
	 * Returns the id of the account with the given name.
	 * 
	 * @param name
	 * @return AccountId
	 * @throws AccountPersistenceServiceException
	 */
	AccountId getIdByName(String name) throws AccountPersistenceServiceException;

	/**
	 * Returns the id of the account with the given email.
	 * 
	 * @param email
	 * @return AccountId
	 * @throws AccountPersistenceServiceException
	 */
	AccountId getIdByEmail(String email) throws AccountPersistenceServiceException;

	/**
	 * Get all account id's.
	 * 
	 * @return {@link Collection} of {@link AccountId}
	 * @throws AccountPersistenceServiceException
	 */
	Collection<AccountId> getAllAccountIds() throws AccountPersistenceServiceException;

	/**
	 * 
	 * @param id
	 * @return List<AccountId>
	 * @throws AccountPersistenceServiceException
	 */
	List<AccountId> getAccountsByType(int id) throws AccountPersistenceServiceException;
	
	/**
	 * Get accounts by query.
	 * 
	 * @param query
	 *            {@link AccountQuery}
	 * @return {@link List} of {@link Account}
	 * @throws AccountPersistenceServiceException
	 */
	List<Account> getAccountsByQuery(AccountQuery query) throws AccountPersistenceServiceException;

}