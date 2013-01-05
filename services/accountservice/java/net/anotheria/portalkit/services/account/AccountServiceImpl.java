package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the account service.
 *
 * @author lrosenberg
 * @since 12.12.12 11:28
 */
public class AccountServiceImpl implements AccountService{

	private AccountServiceConfig config;

	private AccountPersistenceService persistenceService;

	private Cache<AccountId, Account> cache;
	private Cache<AccountId, Account> nonExistingAccountCache;
	private Cache<String, AccountId> name2idCache;
	private Cache<String, AccountId> email2idCache;

	private static final NullAccount NULL_ACCOUNT = NullAccount.INSTANCE;


	public AccountServiceImpl(){
		config = new AccountServiceConfig();

		//TODO cache config
		cache = Caches.createHardwiredCache("accountservice-cache");
		nonExistingAccountCache = Caches.createHardwiredCache("accountservice-nullcache");
		name2idCache = Caches.createHardwiredCache("accountservice-name2id");
		email2idCache = Caches.createHardwiredCache("accountservice-email2id");

		try{
			persistenceService = MetaFactory.get(AccountPersistenceService.class);
		}catch(MetaFactoryException e){
			throw new IllegalStateException("Can't start without persistence service ", e);
		}

	}

	private Account getAccountInternally(AccountId accountId) throws AccountServiceException{
		//first check if we have this account in the cache.
		Account fromCache = cache.get(accountId);
		if (fromCache!=null)
			return fromCache;
		Account notExisting = nonExistingAccountCache.get(accountId);
		if (notExisting!=null)
			return NULL_ACCOUNT;
		try{
			Account fromPersistence = persistenceService.getAccount(accountId);
			if (fromPersistence==null){
				nonExistingAccountCache.put(accountId, NULL_ACCOUNT);
				return NULL_ACCOUNT;
			}
			cache.put(accountId, fromPersistence);
			return fromPersistence;
		}catch(AccountPersistenceServiceException e){
			throw new AccountServiceException(e);
		}
	}

	@Override
	public Account getAccount(AccountId id) throws AccountServiceException {
		Account acc = getAccountInternally(id);
		if (acc==null || acc==NULL_ACCOUNT){
			throw new AccountNotFoundException(id);
		}
		return acc;
	}

	@Override
	public List<Account> getAccounts(List<AccountId> ids) throws AccountServiceException {
		//this method works by iteration. This allows us to preload caches.
		//later on we will provide additional interface for administration purposes that will bypass caching to prevent overload.
		if (ids==null)
			throw new IllegalArgumentException("Null parameter list to getAccount(ids)");
		ArrayList<Account> ret = new ArrayList<Account>(ids.size());
		for (AccountId id : ids){
			Account acc = getAccountInternally(id);
			ret.add(acc); //this will also add the null account.
		}

		return ret;
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountServiceException {
		try{
			persistenceService.deleteAccount(id);
			cache.remove(id);
		}catch(AccountPersistenceServiceException e){
			throw new AccountServiceException(e);
		}
	}

	private void saveAccount(Account toSave) throws AccountServiceException{
		if (toSave.getId()==null)
			throw new IllegalArgumentException("Not account id set, impossible to save "+toSave);
		try{
			persistenceService.saveAccount(toSave);
		}catch(AccountPersistenceServiceException e){
			throw new AccountServiceException(e);
		}

		//we try to
		try{
			cache.put(toSave.getId(), persistenceService.getAccount(toSave.getId()));
		}catch(AccountPersistenceServiceException e){
			//ensure obsolete objects aren't staying in cache.
			cache.remove(toSave.getId());
			throw new AccountServiceException(e);
		}
	}

	@Override
	public Account updateAccount(Account toUpdate) throws AccountServiceException {
		saveAccount(toUpdate);
		return getAccount(toUpdate.getId());
	}

	@Override
	public Account createAccount(Account toCreate) throws AccountServiceException {

		if (config.isExclusiveName() && getAccountIdByNameInternally(toCreate.getName())!=null)
			throw new AccountAlreadyExistsException("name", toCreate.getName());

		Account newAccount = Account.newAccountFromPattern(toCreate);
		saveAccount(newAccount);
		nonExistingAccountCache.remove(newAccount.getId());
		return getAccount(newAccount.getId());
	}

	@Override
	public AccountId getAccountIdByName(String accountName) throws AccountServiceException {
		AccountId id = getAccountIdByNameInternally(accountName);
		if (id==null)
			throw new AccountNotFoundException(accountName);
		return id;
	}

	private AccountId getAccountIdByNameInternally(String accountName) throws AccountServiceException {
		AccountId fromCache = name2idCache.get(accountName);
		if (fromCache!=null)
			return fromCache;
		try{
			AccountId fromPersistence = persistenceService.getIdByName(accountName);
			if (fromPersistence!=null){
				name2idCache.put(accountName,  fromPersistence);
			}
			return fromPersistence;
		}catch(AccountPersistenceServiceException e){
			throw new AccountServiceException(e);
		}
	}

	@Override
	public AccountId getAccountIdByEmail(String accountName) throws AccountServiceException {
		AccountId id = getAccountIdByEmailInternally(accountName);
		if (id==null)
			throw new AccountNotFoundException(accountName);
		return id;
	}

	private AccountId getAccountIdByEmailInternally(String email) throws AccountServiceException {
		AccountId fromCache = email2idCache.get(email);
		if (fromCache!=null)
			return fromCache;
		try{
			AccountId fromPersistence = persistenceService.getIdByEmail(email);
			if (fromPersistence!=null){
				email2idCache.put(email,  fromPersistence);
			}
			return fromPersistence;
		}catch(AccountPersistenceServiceException e){
			throw new AccountServiceException(e);
		}
	}
}
