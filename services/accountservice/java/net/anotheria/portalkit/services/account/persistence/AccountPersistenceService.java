package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 11:41
 */
public interface AccountPersistenceService extends Service {
	/**
	 * Returns the account with that account id.
	 * @param id
	 * @return
	 * @throws AccountPersistenceServiceException
	 */
	Account getAccount(AccountId id) throws AccountPersistenceServiceException;

	/**
	 * Saves the account.
	 * @param account
	 * @throws AccountPersistenceServiceException
	 */
	void saveAccount(Account account) throws AccountPersistenceServiceException;

	/**
	 * Deletes the account with submitted id.
	 * @param id
	 * @throws AccountPersistenceServiceException
	 */
	void deleteAccount(AccountId id) throws AccountPersistenceServiceException;

	/**
	 * Returns the id of the account with the given name.
	 * @param name
	 * @return
	 * @throws AccountPersistenceServiceException
	 */
	AccountId getIdByName(String name) throws AccountPersistenceServiceException;

}
