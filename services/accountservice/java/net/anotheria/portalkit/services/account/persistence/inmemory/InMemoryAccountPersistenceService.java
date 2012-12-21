package net.anotheria.portalkit.services.account.persistence.inmemory;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 20.12.12 09:56
 */
public class InMemoryAccountPersistenceService implements AccountPersistenceService{

	private ConcurrentMap<AccountId, Account> storage = new ConcurrentHashMap<AccountId, Account>();

	@Override
	public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
		return storage.get(id);
	}

	@Override
	public void saveAccount(Account account) throws AccountPersistenceServiceException {
		storage.put(account.getId(), account);
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
		storage.remove(id);
	}
}