package net.anotheria.portalkit.services.account;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import org.distributeme.annotation.DistributeMe;

/**
 * This interface describes the account service, where all system accounts are managed.
 *
 * @author lrosenberg
 * @since 14.10.12 21:56
 */
@DistributeMe()
public interface AccountService extends Service {

	/**
	 * Returns the account for the given accountid.
	 * @param id
	 * @return
	 * @throws AccountServiceException
	 */
	Account getAccount(AccountId id) throws AccountServiceException;

	/**
	 * Returns some accounts.
	 * @param ids ids of accounts to retrieve.
	 * @return
	 * @throws AccountServiceException
	 */
	List<Account> getAccounts(List<AccountId> ids) throws AccountServiceException;
	
	

	/**
	 * Deletes an account.
	 * @param id
	 * @throws AccountServiceException
	 */
	void deleteAccount(AccountId id) throws AccountServiceException;

	/**
	 * Updates existing account.
	 * @param toUpdate
	 * @return
	 * @throws AccountServiceException
	 */
	Account updateAccount(Account toUpdate) throws AccountServiceException;

	/**
	 * Creates a new account with set account id.
	 * @return
	 * @throws AccountServiceException
	 */
	Account createAccount(Account toCreate) throws AccountServiceException;

	/**
	 * Returns accountid for given (login) name. Account name can also be the email adress of the user, or whatever you want
	 * to use for login purposes.
	 * This method returns accountid instead of account object for better scaleability (mod distribution).
	 * @param accountName a value of the name field.
	 * @return
	 * @throws AccountServiceException
	 */
	AccountId getAccountIdByName(String accountName) throws AccountServiceException;

	/**
	 * Returns account id by email.
	 * This method returns accountid instead of account object for better scaleability (mod distribution).
	 * @param accountEmail exact email address.
	 * @return
	 * @throws AccountServiceException
	 */
	AccountId getAccountIdByEmail(String accountEmail) throws AccountServiceException;

}
