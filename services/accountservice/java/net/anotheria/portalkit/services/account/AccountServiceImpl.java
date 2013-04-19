package net.anotheria.portalkit.services.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.account.events.AccountServiceEventAnnouncer;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * The implementation of the account service.
 * 
 * @author lrosenberg
 * @since 12.12.12 11:28
 */
public class AccountServiceImpl implements AccountService, AccountAdminService {

	/**
	 * Config.
	 */
	private AccountServiceConfig config;

	/**
	 * Persistence service.
	 */
	private AccountPersistenceService persistenceService;

	/**
	 * AccountId id->account cache.
	 */
	private Cache<AccountId, Account> cache;

	/**
	 * Cache for not existing accounts, contains null account objects.
	 */
	private Cache<AccountId, Account> nonExistingAccountCache;

	/**
	 * Cache for name 2 id mapping.
	 */
	private Cache<String, AccountId> name2idCache;

	/**
	 * Cache for email 2 id mapping.
	 */
	private Cache<String, AccountId> email2idCache;

	/**
	 * Instance of null account that is used internally.
	 */
	private static final NullAccount NULL_ACCOUNT = NullAccount.INSTANCE;

	/**
	 * Event announcer instance for this service.
	 */
	private AccountServiceEventAnnouncer announcer;

	/**
	 * Default constructor.
	 */
	public AccountServiceImpl() {
		config = new AccountServiceConfig();

		// TODO cache config
		cache = Caches.createHardwiredCache("accountservice-cache");
		nonExistingAccountCache = Caches.createHardwiredCache("accountservice-nullcache");
		name2idCache = Caches.createHardwiredCache("accountservice-name2id");
		email2idCache = Caches.createHardwiredCache("accountservice-email2id");

		try {
			persistenceService = MetaFactory.get(AccountPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}

		announcer = new AccountServiceEventAnnouncer();

	}

	private Account getAccountInternally(AccountId accountId) throws AccountServiceException {
		// first check if we have this account in the cache.
		Account fromCache = cache.get(accountId);
		if (fromCache != null)
			return fromCache.clone();
		Account notExisting = nonExistingAccountCache.get(accountId);
		if (notExisting != null)
			return NULL_ACCOUNT;
		try {
			Account fromPersistence = persistenceService.getAccount(accountId);
			if (fromPersistence == null) {
				nonExistingAccountCache.put(accountId, NULL_ACCOUNT);
				return NULL_ACCOUNT;
			}
			cache.put(accountId, fromPersistence.clone());
			return fromPersistence;
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public Account getAccount(AccountId id) throws AccountServiceException {
		Account acc = getAccountInternally(id);
		if (acc == null || acc == NULL_ACCOUNT) {
			throw new AccountNotFoundException(id);
		}
		return acc;
	}

	@Override
	public List<Account> getAccounts(List<AccountId> ids) throws AccountServiceException {
		// this method works by iteration. This allows us to preload caches.
		// later on we will provide additional interface for administration
		// purposes that will bypass caching to prevent overload.
		if (ids == null)
			throw new IllegalArgumentException("Null parameter list to getAccount(ids)");
		ArrayList<Account> ret = new ArrayList<Account>(ids.size());
		for (AccountId id : ids) {
			Account acc = getAccountInternally(id);
			ret.add(acc); // this will also add the null account.
		}

		return ret;
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountServiceException {
		Account oldAccount = getAccountInternally(id);
		try {
			persistenceService.deleteAccount(id);
			announcer.accountDelete(oldAccount);
			cache.remove(id);
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}

		name2idCache.remove(oldAccount.getName());
		email2idCache.remove(oldAccount.getEmail());
	}

	private void saveAccount(Account toSave) throws AccountServiceException {
		if (toSave.getId() == null)
			throw new IllegalArgumentException("Not account id set, impossible to save " + toSave);
		Account oldAccount = getAccountInternally(toSave.getId());
		try {
			persistenceService.saveAccount(toSave);
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}

		// we try to
		try {
			Account fromPersistence = persistenceService.getAccount(toSave.getId());
			cache.put(toSave.getId(), fromPersistence.clone());
			if (oldAccount != NULL_ACCOUNT) {
				if (!oldAccount.getEmail().equals(fromPersistence.getEmail())) {
					email2idCache.remove(oldAccount.getEmail());
					email2idCache.put(fromPersistence.getEmail(), fromPersistence.getId());
				}
				if (!oldAccount.getName().equals(fromPersistence.getName())) {
					name2idCache.remove(oldAccount.getName());
					name2idCache.put(fromPersistence.getName(), fromPersistence.getId());
				}
			}
		} catch (AccountPersistenceServiceException e) {
			// ensure obsolete objects aren't staying in cache.
			cache.remove(toSave.getId());
			throw new AccountServiceException(e);
		}
	}

	@Override
	public Account updateAccount(Account toUpdate) throws AccountServiceException {
		Account oldAccount = getAccountInternally(toUpdate.getId());
		saveAccount(toUpdate);
		announcer.accountUpdate(oldAccount, toUpdate);
		return getAccount(toUpdate.getId());
	}

	@Override
	public Account createAccount(Account toCreate) throws AccountServiceException {

		if (config.isExclusiveName() && getAccountIdByNameInternally(toCreate.getName()) != null)
			throw new AccountAlreadyExistsException("name", toCreate.getName());
		if (config.isExclusiveMail() && getAccountIdByEmailInternally(toCreate.getEmail()) != null)
			throw new AccountAlreadyExistsException("email", toCreate.getEmail());

		Account newAccount = Account.newAccountFromPattern(toCreate);
		saveAccount(newAccount);
		announcer.accountCreate(newAccount);
		nonExistingAccountCache.remove(newAccount.getId());
		return getAccount(newAccount.getId());
	}

	@Override
	public AccountId getAccountIdByName(String accountName) throws AccountServiceException {
		AccountId id = getAccountIdByNameInternally(accountName);
		if (id == null)
			throw new AccountNotFoundException(accountName);
		return id;
	}

	private AccountId getAccountIdByNameInternally(String accountName) throws AccountServiceException {
		AccountId fromCache = name2idCache.get(accountName);
		if (fromCache != null)
			return fromCache;
		try {
			AccountId fromPersistence = persistenceService.getIdByName(accountName);
			if (fromPersistence != null) {
				name2idCache.put(accountName, fromPersistence);
			}
			return fromPersistence;
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public AccountId getAccountIdByEmail(String accountName) throws AccountServiceException {
		AccountId id = getAccountIdByEmailInternally(accountName);
		if (id == null)
			throw new AccountNotFoundException(accountName);
		return id;
	}

	private AccountId getAccountIdByEmailInternally(String email) throws AccountServiceException {
		AccountId fromCache = email2idCache.get(email);
		if (fromCache != null)
			return fromCache;
		try {
			AccountId fromPersistence = persistenceService.getIdByEmail(email);
			if (fromPersistence != null) {
				email2idCache.put(email, fromPersistence);
			}
			return fromPersistence;
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds() throws AccountAdminServiceException {
		try {
			return persistenceService.getAllAccountIds();
		} catch (AccountPersistenceServiceException e) {
			throw new AccountAdminServiceException(e);
		}
	}

}
