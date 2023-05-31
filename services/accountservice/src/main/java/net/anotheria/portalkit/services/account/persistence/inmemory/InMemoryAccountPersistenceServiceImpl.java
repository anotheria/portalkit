package net.anotheria.portalkit.services.account.persistence.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import org.apache.commons.lang.SerializationUtils;

/**
 * InMemoryAccountPersistenceServiceImpl.
 * 
 * @author lrosenberg
 * @since 20.12.12 09:56
 */
public class InMemoryAccountPersistenceServiceImpl implements AccountPersistenceService {

	/**
	 * Storage instance.
	 */
	private ConcurrentMap<AccountId, Account> storage = new ConcurrentHashMap<AccountId, Account>();

	@Override
	public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
		return Account.class.cast(SerializationUtils.clone(storage.get(id)));
	}

	@Override
	public void saveAccount(Account account) throws AccountPersistenceServiceException {
		storage.put(account.getId().clone(), Account.class.cast(SerializationUtils.clone(account)));
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
		storage.remove(id);
	}

	@Override
	public AccountId getIdByName(String name) throws AccountPersistenceServiceException {
		for (Account acc : storage.values()) {
			System.out.println("checking " + name + " in " + acc);
			if (acc.getName().equals(name))
				return acc.getId().clone();
		}
		return null;
	}

	@Override
	public AccountId getIdByName(String name, String brand) throws AccountPersistenceServiceException {
		for (Account acc : storage.values()) {
			System.out.println("checking " + name + " and " + brand + " in " + acc);
			if (acc.getName().equals(name) && acc.getBrand().equals(brand))
				return acc.getId().clone();
		}
		return null;
	}

	@Override
	public AccountId getIdByEmail(String email) throws AccountPersistenceServiceException {
		for (Account acc : storage.values()) {
			System.out.println("checking " + email + " in " + acc);
			if (acc.getEmail().equals(email))
				return acc.getId().clone();
		}
		return null;
	}

	@Override
	public AccountId getIdByEmail(String email, String brand) throws AccountPersistenceServiceException {
		for (Account acc: storage.values()) {
			System.out.println("checking " + email + " and " + brand + " in " + acc);
			if (acc.getEmail().equals(email) && acc.getBrand().equals(brand))
				return acc.getId().clone();
		}
		return null;
	}

	@Override
	public Collection<AccountId> getAllAccountIds() throws AccountPersistenceServiceException {
		List<AccountId> result = new ArrayList<AccountId>();
		for (AccountId accountId : storage.keySet())
			result.add(accountId.clone());

		return result;
	}

	@Override
	public Collection<AccountId> getAllAccountIds(String brand) throws AccountPersistenceServiceException {
		List<AccountId> result = new ArrayList<>();
		for (Account account: storage.values()) {
			if (account.getBrand().equals(brand))
				result.add(account.getId().clone());
		}
		return result;
	}

	@Override
	public List<AccountId> getAccountsByType(int accountTypeId) throws AccountPersistenceServiceException {
		List<AccountId> result = new ArrayList<AccountId>();
		for (AccountId accountId : storage.keySet()) {
			Account acc = storage.get(accountId);
			if (acc.getType() == accountTypeId) {
				result.add(accountId.clone());
			}
		}
		return result;
	}

	@Override
	public List<Account> getAccountsByQuery(final AccountQuery query) throws AccountPersistenceServiceException {
		throw new UnsupportedOperationException("Implement me.");
	}

}
