package net.anotheria.portalkit.services.authentication.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthPasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthTokenEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MongoTokenDAOImpl implements MongoDAO<BaseEntity> {
    private final Logger log = LoggerFactory.getLogger(MongoTokenDAOImpl.class);

    public MongoTokenDAOImpl() {
    }

    @Override
    public void createEntity(Datastore datastore, BaseEntity entity) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
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
    public void updateEntityPassword(Datastore datastore, BaseEntity entity, String newPassword) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
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
    public BaseEntity getEntity(Datastore datastore, String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
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
    public List<? extends BaseEntity> getAll(Datastore datastore, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        try {
            return datastore.createQuery(entityClass).asList();
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list");
        }

    }

    @Override
    public void deleteEntity(Datastore datastore, String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            datastore.delete(datastore.createQuery(entityClass).field("accid").equal(id));
        } catch (MongoException e) {
            log.error("Can't delete " + entityClass.getSimpleName() + "with accid " + id);
            throw new MongoDaoException("Can't delete " + entityClass.getSimpleName() + "with accid " + id);
        }
    }

    @Override
    public List<? extends BaseEntity> getAllById(Datastore datastore, String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            return datastore.createQuery(entityClass).field("externalId").equal(id).asList();

        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list by id");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list by id");
        }
    }

    @Override
    public BaseEntity getEntityByAccountId(Datastore datastore, String accountId, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (accountId == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
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


    public boolean authTokenExists(Datastore datastore, String token, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (token == null) {
            throw new IllegalArgumentException("Token is null.");
        }
        List<? extends BaseEntity> result = datastore.createQuery(entityClass).field("token").equal(token).asList();
        if (result.isEmpty()) {
            return false;
        }
        return true;
    }


    public Set<String> getTokensByAccountId(Datastore datastore, String id, Class<AuthTokenEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            Set<String> resultSet = new HashSet<>();
            List<? extends BaseEntity> result = datastore.createQuery(entityClass).field("accid").equal(id).asList();
            if (result.isEmpty()) {
                return resultSet;
//                throw new MongoDaoException(entityClass.getSimpleName() + "with acid= " + id + " not found");
            }

            for (BaseEntity baseEntity : result) {
                resultSet.add(((AuthTokenEntity)baseEntity).getToken());
            }

            return resultSet;
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "with accid " + id);
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with accid " + id);
        }
    }

    public void deleteEntityWithToken(Datastore datastore, String id, String token, Class<AuthTokenEntity> entityClass) throws MongoDaoException {
        if (id == null || token == null) {
            throw new IllegalArgumentException("Entity id/token is null.");
        }
        try {
            datastore.delete(datastore.createQuery(entityClass).field("accid").equal(id).field("token").equal(token));
        } catch (MongoException e) {
            log.error("Can't delete " + entityClass.getSimpleName() + "with accid " + id + " and token " + token);
            throw new MongoDaoException("Can't delete " + entityClass.getSimpleName() + "with accid " + id + " and token " + token);
        }
    }
}
