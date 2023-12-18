package net.anotheria.portalkit.services.authentication.persistence.mongo;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.authentication.EncryptedAuthToken;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthPasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthTokenEntity;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseMongoPersistenceServiceImpl;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Monitor(category = "portalkit-persistence-service", subsystem = "authentication")
public class MongoAuthenticationPersistenceServiceImpl extends BaseMongoPersistenceServiceImpl implements AuthenticationPersistenceService{
	private Logger log = LoggerFactory.getLogger(MongoAuthenticationPersistenceServiceImpl.class);

	private final MongoPasswordDAOImpl passwordDao;
	private final MongoTokenDAOImpl tokenDao;

	public MongoAuthenticationPersistenceServiceImpl(){
		super("pk-mongo-auth");
		passwordDao = new MongoPasswordDAOImpl();
		tokenDao = new MongoTokenDAOImpl();
	}


	@Override
	public void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException {
		try {

			Datastore datastore = connect();
			String encryptedPassword = getEncryptedPassword(id);
			if (encryptedPassword == null) {
				//create new entity
				AuthPasswordEntity authPassword = new AuthPasswordEntity(new ObjectId());
				authPassword.setAccid(id.getInternalId());
				authPassword.setPassword(password);
				authPassword.setDaoCreated(System.currentTimeMillis());
				authPassword.setDaoUpdated(System.currentTimeMillis());

				passwordDao.createEntity(datastore, authPassword);
			} else {
				//update existing entity
				AuthPasswordEntity authPasswordEntity = (AuthPasswordEntity) passwordDao.getEntity(datastore, id.getInternalId(), AuthPasswordEntity.class);
				passwordDao.updateEntityPassword(datastore, authPasswordEntity, password);
			}

		} catch (MongoDaoException e) {
			log.error("Can't create password entity", e);
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			AuthPasswordEntity authPasswordEntity = (AuthPasswordEntity) passwordDao.getEntityByAccountId(datastore, id.getInternalId(), AuthPasswordEntity.class);
			if (authPasswordEntity == null) return null;

			return authPasswordEntity.getPassword();
		} catch (MongoDaoException e) {
			log.error("Can't find password by accountId " + id.getInternalId(), e);
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			passwordDao.deleteEntity(datastore, id.getInternalId(), AuthPasswordEntity.class);
		} catch (MongoDaoException e) {
			log.error("Can't delete password with accid= " + id.getInternalId());
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void saveAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			//create new entity
			AuthTokenEntity authToken = new AuthTokenEntity(new ObjectId());
			authToken.setAccid(owner.getInternalId());
			authToken.setToken(encryptedToken);
			authToken.setDaoCreated(System.currentTimeMillis());
			authToken.setDaoUpdated(System.currentTimeMillis());

			tokenDao.createEntity(datastore, authToken);
		} catch (MongoDaoException e) {
			log.error("Can't create campaign", e);
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void saveAuthTokenAdditional(AccountId owner, EncryptedAuthToken encryptedToken) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			//create new entity
			AuthTokenEntity authToken = new AuthTokenEntity(new ObjectId());
			authToken.setAccid(owner.getInternalId());
			authToken.setToken(encryptedToken.getEncryptedVersion());
			authToken.setDaoCreated(System.currentTimeMillis());
			authToken.setDaoUpdated(System.currentTimeMillis());
			authToken.setExpiryTimestamp(encryptedToken.getAuthToken().getExpiryTimestamp());
			authToken.setMultiUse(encryptedToken.getAuthToken().isMultiUse());
			authToken.setExclusive(encryptedToken.getAuthToken().isExclusive());
			authToken.setExclusiveInType(encryptedToken.getAuthToken().isExclusiveInType());
			authToken.setType(encryptedToken.getAuthToken().getType());

			tokenDao.createEntity(datastore, authToken);
		} catch (MongoDaoException e) {
			log.error("Can't create campaign", e);
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			return tokenDao.getTokensByAccountId(datastore, owner.getInternalId(), AuthTokenEntity.class);
		} catch (MongoDaoException e) {
			log.error("Error while getting tokens for accountId", e);
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			return tokenDao.authTokenExists(datastore, encryptedToken, AuthTokenEntity.class);
		} catch (MongoDaoException e) {
			log.error("Error while checking token", e);
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			tokenDao.deleteEntity(datastore, owner.getInternalId(), AuthTokenEntity.class);
		} catch (MongoDaoException e) {
			log.error("Can't delete token with accid= " + owner.getInternalId());
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			tokenDao.deleteEntityWithToken(datastore, owner.getInternalId(), encryptedToken, AuthTokenEntity.class);
		} catch (MongoDaoException e) {
			log.error("Can't delete token with accid= " + owner.getInternalId());
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public long authTokensCount() throws AuthenticationPersistenceServiceException {
		try {
			Datastore datastore = connect();
			return tokenDao.getAuthTokensCount(datastore, AuthTokenEntity.class);
		} catch (MongoDaoException e) {
			log.error("Can't get tokens count");
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}
}
