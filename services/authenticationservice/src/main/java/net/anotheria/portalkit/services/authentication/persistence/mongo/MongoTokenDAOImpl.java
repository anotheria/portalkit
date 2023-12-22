package net.anotheria.portalkit.services.authentication.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthPasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthTokenEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            AuthPasswordEntity authPasswordEntity = (AuthPasswordEntity)entity;
            authPasswordEntity.setPassword(newPassword);
            authPasswordEntity.setDaoUpdated(System.currentTimeMillis());
            datastore.save(authPasswordEntity);
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
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("accid", id)).stream().collect(Collectors.toList());
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
            return datastore.find(entityClass).stream().collect(Collectors.toList());
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
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("accid", id)).stream().collect(Collectors.toList());
            if (result.isEmpty()) {
                return;
            }
            datastore.delete(result.get(0));
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
            return datastore.find(entityClass).filter(Filters.eq("externalId",id)).stream().collect(Collectors.toList());

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
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("accid", accountId)).stream().collect(Collectors.toList());
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
        List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("token",token)).stream().collect(Collectors.toList());
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
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("accid", id)).stream().collect(Collectors.toList());
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
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("accid", id), Filters.eq("token", token)).stream().collect(Collectors.toList());
            if (result.isEmpty()) {
                return;
            }
            datastore.delete(result.get(0));
        } catch (MongoException e) {
            log.error("Can't delete " + entityClass.getSimpleName() + "with accid " + id + " and token " + token);
            throw new MongoDaoException("Can't delete " + entityClass.getSimpleName() + "with accid " + id + " and token " + token);
        }
    }
    public long getAuthTokensCount(Datastore datastore, Class<AuthTokenEntity> entityClass) throws MongoDaoException {
        try {
            return datastore.createQuery(entityClass).countAll();
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list");
        }

    }
}
