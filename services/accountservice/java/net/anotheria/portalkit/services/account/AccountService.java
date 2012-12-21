package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.10.12 21:56
 */
public interface AccountService extends Service {

	/**
	 * Returns the account for the given accountid.
	 * @param id
	 * @return
	 * @throws AccountServiceException
	 */
	Account getAccount(AccountId id) throws AccountServiceException;

	List<Account> getAccounts(List<AccountId> ids) throws AccountServiceException;

	void deleteAccount(AccountId id) throws AccountServiceException;

	Account updateAccount(Account toUpdate) throws AccountServiceException;

	/**
	 * Creates a new account with set account id.
	 * @return
	 * @throws AccountServiceException
	 */
	Account createAccount(Account toCreate) throws AccountServiceException;

}
