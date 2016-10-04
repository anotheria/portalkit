package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * This interface describes the account service, where all system accounts are managed.
 *
 * @author lrosenberg
 * @since 14.10.12 21:56
 */
@DistributeMe()
@FailBy(strategyClass=RetryCallOnce.class)
public interface AccountService extends Service {

	/**
	 * Returns the account for the given accountid.
	 * @param id account id.
	 * @return	{@link Account}
	 * @throws AccountServiceException if error.
	 */
	Account getAccount(AccountId id) throws AccountServiceException;

	/**
	 * Returns some accounts.
	 * @param ids ids of accounts to retrieve.
	 * @return	list of {@link Account}.
	 * @throws AccountServiceException if error.
	 */
	List<Account> getAccounts(List<AccountId> ids) throws AccountServiceException;



	/**
	 * Deletes an account.
	 * @param id account id.
	 * @throws AccountServiceException if error.
	 */
	void deleteAccount(AccountId id) throws AccountServiceException;

	/**
	 * Updates existing account.
	 * @param toUpdate	account.
	 * @return	{@link Account}.
	 * @throws AccountServiceException if error.
	 */
	Account updateAccount(Account toUpdate) throws AccountServiceException;

	/**
	 * Creates a new account with set account id.
	 * @param toCreate	account.
	 * @return	{@link Account}.
	 * @throws AccountServiceException if error.
	 */
	Account createAccount(Account toCreate) throws AccountServiceException;

	/**
	 * Returns accountid for given (login) name. Account name can also be the email adress of the user, or whatever you want
	 * to use for login purposes.
	 * This method returns accountid instead of account object for better scaleability (mod distribution).
	 * @param accountName a value of the name field.
	 * @return	{@link	AccountId}.
	 * @throws AccountServiceException if error.
	 */
	AccountId getAccountIdByName(String accountName) throws AccountServiceException;

	/**
	 * Returns account id by email.
	 * This method returns accountid instead of account object for better scaleability (mod distribution).
	 * @param accountEmail exact email address.
	 * @return	{@link	AccountId}.
	 * @throws AccountServiceException if error.
	 */
	AccountId getAccountIdByEmail(String accountEmail) throws AccountServiceException;

}
