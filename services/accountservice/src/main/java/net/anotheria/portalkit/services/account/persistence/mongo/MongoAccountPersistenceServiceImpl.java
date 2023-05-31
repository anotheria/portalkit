package net.anotheria.portalkit.services.account.persistence.mongo;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.account.persistence.mongo.entities.AccountEntity;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseMongoPersistenceServiceImpl;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Monitor(category = "portalkit-persistence-service", subsystem = "account")
public class MongoAccountPersistenceServiceImpl extends BaseMongoPersistenceServiceImpl implements AccountPersistenceService {
	private Logger log = LoggerFactory.getLogger(MongoAccountPersistenceServiceImpl.class);

	private final MongoAccountDAOImpl accountDao;

	public MongoAccountPersistenceServiceImpl(){
		super("pk-mongo-account");
		accountDao = new MongoAccountDAOImpl();
	}


	@Override
	public void saveAccount(Account account) throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			Account savedAccount = getAccount(account.getId());
			if (savedAccount == null) {
				//create new entity
				AccountEntity accountEntity = new AccountEntity(new ObjectId());
				accountEntity.setAccid(account.getId().getInternalId());
				accountEntity.setEmail(account.getEmail());
				accountEntity.setName(account.getName());
				accountEntity.setRegts(account.getRegistrationTimestamp());
				accountEntity.setStatus(account.getStatus());
				accountEntity.setTenant(account.getTenant());
				accountEntity.setType(account.getType());
				accountEntity.setBrand(account.getBrand());
				accountEntity.setDaoCreated(System.currentTimeMillis());
				accountEntity.setDaoUpdated(System.currentTimeMillis());

				accountDao.createEntity(datastore, accountEntity);
			} else {
				//update existing entity
				AccountEntity accountEntity = (AccountEntity) accountDao.getEntity(datastore, account.getId().getInternalId(), AccountEntity.class);
				accountDao.updateEntityFields(datastore, accountEntity, account);
			}

		} catch (MongoDaoException e) {
			log.error("Can't create account", e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			AccountEntity accountEntity = (AccountEntity) accountDao.getEntityByAccountId(datastore, id.getInternalId(), AccountEntity.class);
			if (accountEntity == null) return null;

			return accountEntity.toAccout();
		} catch (MongoDaoException e) {
			log.error("Can't find account by accountId " + id.getInternalId(), e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			accountDao.deleteEntity(datastore, id.getInternalId(), AccountEntity.class);
		} catch (MongoDaoException e) {
			log.error("Can't delete account with accid= " + id.getInternalId());
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public AccountId getIdByName(String name) throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			AccountEntity accountEntity = (AccountEntity) accountDao.getAccountByName(datastore, name, AccountEntity.class);
			if (accountEntity == null) return null;

			return new AccountId(accountEntity.getAccid());
		} catch (MongoDaoException e) {
			log.error("Can't find accountId by name " + name, e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public AccountId getIdByName(String name, String brand) throws AccountPersistenceServiceException {
		try {
			AccountEntity accountEntity = (AccountEntity) accountDao.getAccountByName(connect(), name, brand, AccountEntity.class);
			if (accountEntity == null) return null;

			return new AccountId(accountEntity.getAccid());
		} catch (MongoDaoException e) {
			log.error("Can't find accountId by name " + name + " and brand " + brand, e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public AccountId getIdByEmail(String email) throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			AccountEntity accountEntity = (AccountEntity) accountDao.getAccountByEmail(datastore, email, AccountEntity.class);
			if (accountEntity == null) return null;

			return new AccountId(accountEntity.getAccid());
		} catch (MongoDaoException e) {
			log.error("Can't find accountId by email " + email, e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public AccountId getIdByEmail(String email, String brand) throws AccountPersistenceServiceException {
		try {
			AccountEntity accountEntity = (AccountEntity) accountDao.getAccountByEmail(connect(), email, brand, AccountEntity.class);
			if (accountEntity == null) return null;

			return new AccountId(accountEntity.getAccid());
		} catch (MongoDaoException e) {
			log.error("Can't find accountId by email " + email + " and brand " + brand, e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds() throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			List<? extends BaseEntity> allAccounts = accountDao.getAll(datastore, AccountEntity.class);

			Set<AccountId> resultSet = new HashSet<>();
			for (BaseEntity baseEntity : allAccounts) {
				resultSet.add(
					new AccountId(((AccountEntity)baseEntity).getAccid())
				);
			}

			return resultSet;
		} catch (MongoDaoException e) {
			log.error("Error while getting tokens for accountId", e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds(String brand) throws AccountPersistenceServiceException {
		try {
			List<? extends BaseEntity> allAccounts = accountDao.getAllAccounts(connect(), AccountEntity.class);
			Set<AccountId> set = new HashSet<>();
			for (BaseEntity baseEntity: allAccounts)
				set.add(new AccountId(((AccountEntity)baseEntity).getAccid()));

			return set;
		} catch (MongoDaoException e) {
			log.error("Error while getting accountIds for brand " + brand, e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<AccountId> getAccountsByType(int type) throws AccountPersistenceServiceException {
		try {
			Datastore datastore = connect();
			List<? extends BaseEntity> accountsByType = accountDao.getAccountsByType(datastore, type, AccountEntity.class);
			if (accountsByType == null) return null;

			List<AccountId> resultList = new ArrayList<>();
			for (BaseEntity baseEntity : accountsByType) {
				resultList.add(
						new AccountId(((AccountEntity)baseEntity).getAccid())
				);
			}

			return resultList;

		} catch (MongoDaoException e) {
			log.error("Can't find accountId by type " + type, e);
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<Account> getAccountsByQuery(AccountQuery query) throws AccountPersistenceServiceException {
		return null;
	}

}
