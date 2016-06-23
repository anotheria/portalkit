package net.anotheria.portalkit.services.authentication.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthPasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.BaseEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.MongoConnector;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MongoPasswordDAOImpl implements MongoDAO<BaseEntity> {

    private final MongoConnector connector;

    private final Logger log;

    public MongoPasswordDAOImpl() {
        connector = MongoConnector.INSTANCE;
        log = LoggerFactory.getLogger(MongoPasswordDAOImpl.class);
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
            log.error("Email already exists " + entity);
            throw new MongoDaoException("Email already exists");
        } catch (MongoException e) {
            log.error("Can't store " + entity.toString());
            throw new MongoDaoException("Can't store " + entity.toString());
        }

    }

    @Override
    public void updateEntityPassword(BaseEntity entity, String newPassword) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
            Datastore datastore = connector.connect();
            // change the name of the hotel
            UpdateOperations<AuthPasswordEntity> ops = datastore.createUpdateOperations(AuthPasswordEntity.class)
                    .set("password", newPassword)
                    .set("daoUpdated", System.currentTimeMillis());
            datastore.update((AuthPasswordEntity) entity, ops);
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


}
