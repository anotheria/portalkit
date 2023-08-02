package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.event.AccountServiceEventSupplier;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceService;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The implementation of the account service.
 * 
 * @author lrosenberg
 * @since 12.12.12 11:28
 */
@Monitor (subsystem = "account", category = "portalkit-service")
public enum AccountServiceImpl implements AccountService, AccountAdminService {
	INSTANCE;

	/**
	 * Config.
	 */
	private AccountServiceConfig config;

	/**
	 * Persistence service.
	 */
	private AccountPersistenceService persistenceService;

	/**
	 * {@link AccountAuditPersistenceService} service.
	 */
	private AccountAuditPersistenceService accountAuditPersistenceService;

	/**
	 * {@link AccountServiceEventSupplier} instance.
	 */
	private final AccountServiceEventSupplier eventSupplier = new AccountServiceEventSupplier();

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
	 * Cache for name and brand 2 id mapping.
	 */
	private Cache<String, AccountId> nameAndBrand2idCache;

	/**
	 * Cache for email and brand 2 id mapping.
	 */
	private Cache<String, AccountId> emailAndBrand2idCache;

	/**
	 * Instance of null account that is used internally.
	 */
	private static final NullAccount NULL_ACCOUNT = NullAccount.INSTANCE;

	/**
	 * Default constructor.
	 */
	private AccountServiceImpl() {
		init();
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
		Account oldAccount = getAccount(id);
		try {
			persistenceService.deleteAccount(id);
			cache.remove(id);
			eventSupplier.accountDeleted(oldAccount);
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}

		if (config.isBrandEnabled()) {
			nameAndBrand2idCache.remove(getBrandKey(oldAccount.getName(), oldAccount.getBrand()));
			emailAndBrand2idCache.remove(getBrandKey(oldAccount.getEmail(), oldAccount.getBrand()));
		} else {
			name2idCache.remove(oldAccount.getName());
			email2idCache.remove(oldAccount.getEmail());
		}
	}

	private void saveAccount(Account toSave) throws AccountServiceException {
		if (toSave.getId() == null)
			throw new IllegalArgumentException("Not account id set, impossible to save " + toSave);
		Account oldAccount = getAccountInternally(toSave.getId());

		if (config.isBrandEnabled() && StringUtils.isEmpty(toSave.getBrand()))
			toSave.setBrand(config.getDefaultBrand());

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
					if (config.isBrandEnabled()) {
						emailAndBrand2idCache.remove(getBrandKey(oldAccount.getEmail(), oldAccount.getBrand()));
						emailAndBrand2idCache.put(getBrandKey(fromPersistence.getEmail(), fromPersistence.getBrand()), fromPersistence.getId());
					} else {
						email2idCache.remove(oldAccount.getEmail());
						email2idCache.put(fromPersistence.getEmail(), fromPersistence.getId());
					}
				}
				if (!oldAccount.getName().equals(fromPersistence.getName())) {
					if (config.isBrandEnabled()) {
						nameAndBrand2idCache.remove(getBrandKey(oldAccount.getName(), oldAccount.getBrand()));
						nameAndBrand2idCache.put(getBrandKey(fromPersistence.getName(), fromPersistence.getBrand()), fromPersistence.getId());
					} else {
						name2idCache.remove(oldAccount.getName());
						name2idCache.put(fromPersistence.getName(), fromPersistence.getId());
					}
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
		if(config.isAuditEnabled()) {
			createAuditForAccount(oldAccount, toUpdate);
		}
		eventSupplier.accountUpdated(oldAccount, toUpdate);
		return getAccount(toUpdate.getId());
	}

	@Override
	public Account createAccount(Account toCreate) throws AccountServiceException {

		if (config.isBrandEnabled()) {
			if (config.isExclusiveName() && getAccountIdByNameInternally(toCreate.getName(), toCreate.getBrand()) != null)
				throw new AccountAlreadyExistsException("name", toCreate.getName(), toCreate.getBrand());
			if (config.isExclusiveMail() && getAccountIdByEmailInternally(toCreate.getEmail(), toCreate.getBrand()) != null)
				throw new AccountAlreadyExistsException("email", toCreate.getEmail(), toCreate.getBrand());
		} else {
			if (config.isExclusiveName() && getAccountIdByNameInternally(toCreate.getName()) != null)
				throw new AccountAlreadyExistsException("name", toCreate.getName());
			if (config.isExclusiveMail() && getAccountIdByEmailInternally(toCreate.getEmail()) != null)
				throw new AccountAlreadyExistsException("email", toCreate.getEmail());
		}

		toCreate.setId(AccountId.generateNew());
		saveAccount(toCreate);
		if (config.isAuditEnabled()) {
			createAuditForAccount(toCreate);
		}
		eventSupplier.accountCreated(toCreate);
		nonExistingAccountCache.remove(toCreate.getId());
		return getAccount(toCreate.getId());
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
	public AccountId getAccountIdByName(String accountName, String brand) throws AccountServiceException {
		AccountId id = getAccountIdByNameInternally(accountName, brand);
		if (id == null)
			throw new AccountNotFoundException(accountName, brand);
		return id;
	}

	private AccountId getAccountIdByNameInternally(String accountName, String brand) throws AccountServiceException {
		AccountId fromCache = nameAndBrand2idCache.get(getBrandKey(accountName, brand));
		if (fromCache != null)
			return fromCache;

		try {
			AccountId fromPersistence = persistenceService.getIdByName(accountName, brand);
			if (fromPersistence != null)
				nameAndBrand2idCache.put(getBrandKey(accountName, brand), fromPersistence);

			return fromPersistence;
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public AccountId getAccountIdByEmail(String accountEmail, String brand) throws AccountServiceException {
		AccountId id = getAccountIdByEmailInternally(accountEmail, brand);
		if (id == null)
			throw new AccountNotFoundException(accountEmail, brand);
		return id;
	}

	private AccountId getAccountIdByEmailInternally(String accountEmail, String brand) throws AccountServiceException{
		AccountId fromCache = emailAndBrand2idCache.get(getBrandKey(accountEmail, brand));
		if (fromCache != null)
			return fromCache;

		try {
			AccountId fromPersistence = persistenceService.getIdByEmail(accountEmail, brand);
			if (fromPersistence != null)
				emailAndBrand2idCache.put(getBrandKey(accountEmail, brand), fromPersistence);

			return fromPersistence;
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds(String brand) throws AccountAdminServiceException {
		try {
			return persistenceService.getAllAccountIds(brand);
		} catch (AccountPersistenceServiceException e) {
			throw new AccountAdminServiceException(e);
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

	@Override
	public List<AccountId> getAccountsByType(@SuppressWarnings("rawtypes") AccountType accountType) throws AccountServiceException {
		try {
			return persistenceService.getAccountsByType(accountType.getId());
		} catch (AccountPersistenceServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public List<Account> getAccountsByQuery(final AccountQuery query) throws AccountAdminServiceException {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		try {
			return persistenceService.getAccountsByQuery(query);
		} catch (AccountPersistenceServiceException e) {
			throw new AccountAdminServiceException(e);
		}
	}

	private void createAuditForAccount(Account account) throws AccountServiceException {
		saveAccountAudit(account.getId(), 0L, account.getStatus(), 0L, account.getStatus(), System.currentTimeMillis());
	}

	private void createAuditForAccount(Account oldAccount, Account newAccount) throws AccountServiceException {
		if (!oldAccount.getId().equals(newAccount.getId())) {
			return;
		}

		long timestamp = System.currentTimeMillis();

		for (int i = 0; i <= 64; i++) {
			long currentStatus = (long) Math.pow(2, i);
			if (oldAccount.hasStatus(currentStatus) && !newAccount.hasStatus(currentStatus)) {
				saveAccountAudit(newAccount.getId(), oldAccount.getStatus(), newAccount.getStatus(), currentStatus, 0L, timestamp);
			}
			if (!oldAccount.hasStatus(currentStatus) && newAccount.hasStatus(currentStatus)) {
				saveAccountAudit(newAccount.getId(), oldAccount.getStatus(), newAccount.getStatus(),0L, currentStatus, timestamp);
			}
		}
	}

	private void saveAccountAudit(AccountId accountId, long oldStatus, long newStatus, long statusRemoved, long statusAdded, long timestamp) throws AccountServiceException {

		AccountAudit accountAudit = new AccountAudit();
		accountAudit.setAccountId(accountId);
		accountAudit.setOldStatus(oldStatus);
		accountAudit.setNewStatus(newStatus);
		accountAudit.setStatusRemoved(statusRemoved);
		accountAudit.setStatusAdded(statusAdded);
		accountAudit.setCreated(timestamp);

		try {
			accountAuditPersistenceService.saveAccountAudit(accountAudit);
		} catch (AccountAuditPersistenceServiceException e) {
			throw new AccountServiceException("Fail save account audit", e);
		}
	}

	@Override
	public List<AccountAudit> getAccountAudits(AccountId accountId) throws AccountAdminServiceException {
		if (!config.isAuditEnabled()) {
			return null;
		}

		try {
			return accountAuditPersistenceService.getAccountAudits(accountId);
		} catch (AccountAuditPersistenceServiceException e) {
			throw new AccountAdminServiceException("Account audit persistence error", e);
		}
	}

	private void init(){
		config = AccountServiceConfig.getInstance();

		cache = Caches.createConfigurableHardwiredCache("pk-cache-account-service");
		nonExistingAccountCache = Caches.createConfigurableHardwiredCache("pk-cache-null-account-service");

		if (config.isBrandEnabled()) {
			nameAndBrand2idCache = Caches.createConfigurableHardwiredCache("pk-cache-accountservice-namebrand2id");
			emailAndBrand2idCache = Caches.createConfigurableHardwiredCache("pk-cache-accountservice-emailbrand2id");
		} else {
			name2idCache = Caches.createConfigurableHardwiredCache("pk-cache-accountservice-name2id");
			email2idCache = Caches.createConfigurableHardwiredCache("pk-cache-accountservice-email2id");
		}

		try {
			persistenceService = MetaFactory.get(AccountPersistenceService.class);
			if (config.isAuditEnabled()) {
				accountAuditPersistenceService = MetaFactory.get(AccountAuditPersistenceService.class);
			}
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}

	}

	void unitTestReset(){
		init();
	}

	private String getBrandKey(String value, String brand) {
		return value + "_" + brand;
	}
}
