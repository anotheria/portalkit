package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 11:41
 */
public interface AccountPersistenceService {
	Account getAccount(AccountId id) throws AccountPersistenceServiceException;

	void saveAccount(Account account) throws AccountPersistenceServiceException;

	Account createAccount() throws AccountPersistenceServiceException;
}
