package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.10.12 21:56
 */
public interface AccountService {

	Account getAccount(AccountId id) throws AccountServiceException;

	List<Account> getAccounts(List<AccountId> ids) throws AccountServiceException;

	void deleteAccount(AccountId id) throws AccountServiceException;

	Account updateAccount(Account toUpdate) throws AccountServiceException;

	/**
	 * Creates a new account with set account id.
	 * @return
	 * @throws AccountServiceException
	 */
	Account createAccount() throws AccountServiceException;

}
