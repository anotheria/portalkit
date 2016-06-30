package net.anotheria.portalkit.services.account.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.mongo.entities.AccountEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.MongoConnector;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MongoAccountDAOImpl implements MongoAccountDAO<BaseEntity> {

    private final MongoConnector connector;

    private final Logger log;

    public MongoAccountDAOImpl() {
        connector = MongoConnector.INSTANCE;
        log = LoggerFactory.getLogger(MongoAccountDAOImpl.class);
    }

    @Override
    public void createEntity(BaseEntity entity) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
            Datastore datastore = connector.connect();
            datastore.save(entity);
        } catch (DuplicateKeyException e) {
            log.error("id already exists " + entity);
            throw new MongoDaoException("Id already exists");
        } catch (MongoException e) {
            log.error("Can't store " + entity.toString());
            throw new MongoDaoException("Can't store " + entity.toString());
        }

    }

    @Override
    public void updateEntityFields(BaseEntity entity, Account newAccountData) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
            Datastore datastore = connector.connect();
            // change the name of the hotel
            UpdateOperations<AccountEntity> ops = datastore.createUpdateOperations(AccountEntity.class)
                    .set("name", newAccountData.getName())
                    .set("email", newAccountData.getEmail())
                    .set("type", newAccountData.getType())
                    .set("tenant", newAccountData.getTenant())
                    .set("regts", newAccountData.getRegistrationTimestamp())
                    .set("status", newAccountData.getStatus())
                    .set("daoUpdated", System.currentTimeMillis());
            datastore.update((AccountEntity) entity, ops);
        } catch (MongoException e) {
            log.error("Can't update " + entity.toString());
            throw new MongoDaoException("Can't update " + entity.toString());
        }
    }

    @Override
    public BaseEntity getEntity(String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            Datastore datastore = connector.connect();
            List<? extends BaseEntity> result = datastore.createQuery(entityClass).field("accid").equal(id).asList();
            if (result.isEmpty()) {
                throw new MongoDaoException(entityClass.getSimpleName() + "with acid= " + id + " not found");
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "with accid " + id);
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with accid " + id);
        }
    }

    @Override
    public List<? extends BaseEntity> getAll(Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        try {
            Datastore datastore = connector.connect();
            return datastore.createQuery(entityClass).asList();
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list");
        }

    }

    @Override
    public void deleteEntity(String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            Datastore datastore = connector.connect();
            datastore.delete(datastore.createQuery(entityClass).field("accid").equal(id));
        } catch (MongoException e) {
            log.error("Can't delete " + entityClass.getSimpleName() + "with accid " + id);
            throw new MongoDaoException("Can't delete " + entityClass.getSimpleName() + "with accid " + id);
        }
    }

    @Override
    public List<? extends BaseEntity> getAllById(String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            Datastore datastore = connector.connect();
            return datastore.createQuery(entityClass).field("externalId").equal(id).asList();

        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list by id");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list by id");
        }
    }

    @Override
    public BaseEntity getEntityByAccountId(String accountId, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (accountId == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
            Datastore datastore = connector.connect();
            List<? extends BaseEntity> result = datastore.createQuery(entityClass).field("accid").equal(accountId).asList();
            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "with accid: " + accountId);
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with accid " + accountId);
        }
    }

    @Override
    public BaseEntity getAccountByName(String name, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (name == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
            Datastore datastore = connector.connect();
            List<? extends BaseEntity> result = datastore.createQuery(entityClass).field("name").equal(name).asList();
            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "with name: " + name);
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with name " + name);
        }
    }

    @Override
    public BaseEntity getAccountByEmail(String email, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (email == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
            Datastore datastore = connector.connect();
            List<? extends BaseEntity> result = datastore.createQuery(entityClass).field("email").equal(email).asList();
            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "with email: " + email);
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with email " + email);
        }
    }

    @Override
    public List<? extends BaseEntity> getAllAccounts(Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        return null;
    }

    @Override
    public List<? extends BaseEntity> getAccountsByType(int type, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        try {
            Datastore datastore = connector.connect();
            return datastore.createQuery(entityClass).field("type").equal(type).asList();

        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list by type");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list by type");
        }
    }

}
