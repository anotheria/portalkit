package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.account.event.AccountServiceEventSupplier;
import net.anotheria.portalkit.services.account.persistence.*;
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
public class AccountServiceImpl implements AccountService, AccountAdminService, EntityManagingService {

	/**
	 * Config.
	 */
	private AccountServiceConfig config;


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

	// mongo repositories.
	private final AccountEntityRepository accountEntityRepository;
	private final AccountNoteEntityRepository accountNoteEntityRepository;
	private final AccountAuditEntityRepository accountAuditEntityRepository;

	/**
	 * Default constructor.
	 */
	AccountServiceImpl(AccountEntityRepository accountEntityRepository,
					   AccountNoteEntityRepository accountNoteEntityRepository,
					   AccountAuditEntityRepository accountAuditEntityRepository) {
		this.accountEntityRepository = accountEntityRepository;
		this.accountNoteEntityRepository = accountNoteEntityRepository;
		this.accountAuditEntityRepository = accountAuditEntityRepository;
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
		AccountEntity entity = null;
		try{
			entity = accountEntityRepository.findById(accountId.getInternalId()).orElse(null);
		}catch(Exception e){
			throw new AccountServiceException("Mongo read failed for " + accountId, e);
		}
		if (entity == null) {
			nonExistingAccountCache.put(accountId, NULL_ACCOUNT);
			return NULL_ACCOUNT;
		}
		Account fromPersistence = entity.toAccount();
		cache.put(accountId, fromPersistence.clone());
		return fromPersistence;
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
			accountEntityRepository.deleteById(id.getInternalId());
			cache.remove(id);
			eventSupplier.accountDeleted(oldAccount);
		} catch (Exception e) {
			throw new AccountServiceException("Mongo delete failed: "+e.getMessage(), e);
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
			AccountEntity entity = new AccountEntity(toSave);
			accountEntityRepository.save(entity);
		} catch (Exception e) {
			throw new AccountServiceException("Mongo save failed");
		}

		// we try to
		try {
			Account fromPersistence = accountEntityRepository.findById(toSave.getId().getInternalId()).orElse(null).toAccount();
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
		} catch (Exception e) {
			// ensure obsolete objects aren't staying in cache.
			cache.remove(toSave.getId());
			throw new AccountServiceException("Mongo read after save failed", e);
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
			String repositoryAccountId = accountEntityRepository.findIdByName(accountName).map(IDOnly::getId).orElse(null);
			AccountId fromPersistence = repositoryAccountId == null ? null : AccountId.fromUDID(repositoryAccountId);
			if (fromPersistence != null) {
				name2idCache.put(accountName, fromPersistence);
			}
			return fromPersistence;
		} catch (Exception e) {
			throw new AccountServiceException("Mongo read failed for name: " + accountName, e);
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
			String repositoryAccountId = accountEntityRepository.findIdByEmail(email).map(IDOnly::getId).orElse(null);
			AccountId fromPersistence = repositoryAccountId == null ? null : AccountId.fromUDID(repositoryAccountId);
			if (fromPersistence != null) {
				email2idCache.put(email, fromPersistence);
			}
			return fromPersistence;
		} catch (Exception e) {
			throw new AccountServiceException("Mongo read failed for email: " + email, e);
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
			String repositoryAccountId = accountEntityRepository.findIdByNameAndBrand(accountName, brand).map(IDOnly::getId).orElse(null);
			AccountId fromPersistence = repositoryAccountId == null ? null : AccountId.fromUDID(repositoryAccountId);
			if (fromPersistence != null)
				nameAndBrand2idCache.put(getBrandKey(accountName, brand), fromPersistence);

			return fromPersistence;
		} catch (Exception e) {
			throw new AccountServiceException("Mongo read failed for name and brand: " + accountName + ", " + brand, e);
		}
	}

	@Override
	public AccountId getAccountIdByEmail(String accountEmail, String brand) throws AccountServiceException {
		AccountId id = getAccountIdByEmailInternally(accountEmail, brand);
		if (id == null)
			throw new AccountNotFoundException(accountEmail, brand);
		return id;
	}

	@Override
	public void saveAccountNote(AccountNote accountNote) throws AccountServiceException {
		try {
			AccountNoteEntity newEntity = AccountNoteEntity.createFromAccountNote(accountNote);
			accountNoteEntityRepository.save(newEntity);
		} catch (Exception e) {
			throw new AccountServiceException("Can't save account note for account id: " + accountNote.getAccountId(), e);
		}
	}

	@Override
	public AccountNote getAccountNoteById(String id) throws AccountServiceException {
		try {
			AccountNoteEntity entity = accountNoteEntityRepository.findById(id).orElse(null);
			return entity == null ? null : entity.toAccountNote();
		} catch (Exception e) {
			throw new AccountServiceException("Mongo failed to get account note by id: " + id, e);
		}
	}

	@Override
	public AccountNote updateAccountNote(AccountNote accountNote) throws AccountServiceException {
		try {
			AccountNoteEntity existingEntity = accountNoteEntityRepository.findById(accountNote.getId()).orElse(null);
			if (existingEntity == null)
				throw new AccountServiceException("Account note with id " + accountNote.getId() + " does not exist and thus cannot be updated.");
			existingEntity.updateFromAccountNote(accountNote);
			accountNoteEntityRepository.save(existingEntity);
			return existingEntity.toAccountNote();
		} catch (Exception e) {
			throw new AccountServiceException("Mongo failed to update account note with id " + accountNote.getId(), e);
		}
	}

	@Override
	public void deleteAccountNote(String id) throws AccountServiceException {
		try {
			accountNoteEntityRepository.deleteById(id);
		} catch (Exception e) {
			throw new AccountServiceException("Mongo failed to delete account note by id: " + id, e);
		}
	}

	@Override
	public List<AccountNote> getNotesByAccountId(AccountId accountId) throws AccountServiceException {
		try {
			List<AccountNoteEntity> entities =
					accountNoteEntityRepository.findAllByAccountId(accountId.getInternalId());
			List<AccountNote> notes = new ArrayList<>();
			for (AccountNoteEntity entity : entities) {
				notes.add(entity.toAccountNote());
			}
			return notes;
		} catch (Exception e) {
			throw new AccountServiceException("Mongo failed to get account notes by account id: " + accountId, e);
		}
	}

	private AccountId getAccountIdByEmailInternally(String accountEmail, String brand) throws AccountServiceException{
		AccountId fromCache = emailAndBrand2idCache.get(getBrandKey(accountEmail, brand));
		if (fromCache != null)
			return fromCache;

		try {
			String entityAccountId = accountEntityRepository.findIdByEmailAndBrand(accountEmail, brand).map(IDOnly::getId).orElse(null);
			AccountId fromPersistence = entityAccountId == null ? null : AccountId.fromUDID(entityAccountId);
			if (fromPersistence != null)
				emailAndBrand2idCache.put(getBrandKey(accountEmail, brand), fromPersistence);

			return fromPersistence;
		} catch (Exception e) {
			throw new AccountServiceException("Mongo read failed for email and brand: " + accountEmail + ", " + brand, e);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds(String brand) throws AccountAdminServiceException {
		try {
			List<IDOnly> accountIdsFromEntity = accountEntityRepository.findAllIdsByBrand(brand);
			List<AccountId> accountIds = new ArrayList<>();
			for (IDOnly id : accountIdsFromEntity) {
				accountIds.add(AccountId.fromUDID(id.getId()));
			}
			return accountIds;
		} catch (Exception e) {
			throw new AccountAdminServiceException("AccountIds retrieval failed", e);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds() throws AccountAdminServiceException {
		try {
			List<IDOnly> accountIdsFromEntity = accountEntityRepository.findAllIds();
			List<AccountId> accountIds = new ArrayList<>();
			for (IDOnly id : accountIdsFromEntity) {
				accountIds.add(AccountId.fromUDID(id.getId()));
			}
			return accountIds;
		} catch (Exception e) {
			throw new AccountAdminServiceException("AccountIds retrieval failed", e);
		}
	}

	@Override
	public List<AccountId> getAccountsByType(@SuppressWarnings("rawtypes") AccountType accountType) throws AccountServiceException {
		try {
			List<IDOnly> accountIdsFromEntity = accountEntityRepository.findAllIdsByType(accountType.getId());
			List<AccountId> accountIds = new ArrayList<>();
			for (IDOnly id : accountIdsFromEntity) {
				accountIds.add(new AccountId(id.getId()));
			}
			return accountIds;
		} catch (Exception e) {
			throw new AccountServiceException("AccountIds retrieval by type failed", e);
		}
	}

	@Override
	public List<Account> getAccountsByQuery(final AccountQuery query) throws AccountAdminServiceException {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");
		List<AccountEntity> accountEntities = accountEntityRepository.search(query);
		List<Account> accounts = new ArrayList<>();
		for (AccountEntity entity : accountEntities) {
			accounts.add(entity.toAccount());
		}
		return accounts;
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
			accountAuditEntityRepository.save(AccountAuditEntity.createFromAccountAudit(accountAudit));
		} catch (Exception e) {
			throw new AccountServiceException("Fail save account audit", e);
		}
	}

	@Override
	public List<AccountAudit> getAccountAudits(AccountId accountId) throws AccountAdminServiceException {
		if (!config.isAuditEnabled()) {
			return null;
		}

		try {
			List<AccountAuditEntity> auditEntities = accountAuditEntityRepository.findAllByAccountId(accountId.getInternalId());

			List<AccountAudit> audits = new ArrayList<>();
			for (AccountAuditEntity entity : auditEntities) {
				audits.add(entity.toAccountAudit());
			}
			return audits;
		} catch (Exception e) {
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

		EntityManagingServices.createEntityCounter(this, "Accounts");
	}

	void unitTestReset(){
		init();
	}

	private String getBrandKey(String value, String brand) {
		return value + "_" + brand;
	}

	@Override
	public int getEntityCount(String s) {
		try {
			return accountEntityRepository.findAllIds().size();
		} catch (Exception e) {
			//we swallow the exception, cause this is for monitoring purposes only and the tool shouldn't fail.
			return 0;
		}
	}


}
