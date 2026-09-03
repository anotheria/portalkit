package net.anotheria.portalkit.services.authentication.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import dev.morphia.Datastore;
import dev.morphia.UpdateOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperators;
import net.anotheria.portalkit.services.authentication.TokenInventoryEntry;
import net.anotheria.portalkit.services.authentication.TokenObfuscator;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthPasswordEntity;
import net.anotheria.portalkit.services.authentication.persistence.mongo.entities.AuthTokenEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
            return datastore.find(entityClass).count();
        } catch (MongoException e) {
            log.error("Can't find " + entityClass.getSimpleName() + "list");
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list");
        }

    }

    /**
     * Sets the last used timestamp of the given token, but only if the stored one is older than the given
     * threshold or not present at all. Documents written before the field existed do not carry it, and mongo
     * does not match a missing field with a comparison, hence the explicit exists filter.
     *
     * @param datastore    datastore.
     * @param token        the token which was used.
     * @param timestamp    timestamp to store.
     * @param onlyIfOlderThan store only if the currently stored value is smaller than this.
     * @param entityClass  entity class.
     * @throws MongoDaoException if error.
     */
    public void updateLastUsed(Datastore datastore, String token, long timestamp, long onlyIfOlderThan, Class<AuthTokenEntity> entityClass) throws MongoDaoException {
        if (token == null) {
            throw new IllegalArgumentException("Token is null.");
        }
        try {
            datastore.find(entityClass)
                    .filter(Filters.eq("token", token),
                            Filters.or(Filters.lt("lastUsedAt", onlyIfOlderThan), Filters.exists("lastUsedAt").not()))
                    .update(new UpdateOptions(), UpdateOperators.set("lastUsedAt", timestamp));
        } catch (MongoException e) {
            log.error("Can't update last used timestamp", e);
            throw new MongoDaoException("Can't update last used timestamp");
        }
    }

    /**
     * Returns the inventory entries of all tokens of the given account.
     *
     * @param datastore   datastore.
     * @param accountId   the account id.
     * @param entityClass entity class.
     * @return the entries, never null.
     * @throws MongoDaoException if error.
     */
    public List<TokenInventoryEntry> getTokenInventoryByAccount(Datastore datastore, String accountId, Class<AuthTokenEntity> entityClass) throws MongoDaoException {
        if (accountId == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
            List<TokenInventoryEntry> ret = new ArrayList<>();
            for (AuthTokenEntity entity : datastore.find(entityClass).filter(Filters.eq("accid", accountId)).stream().collect(Collectors.toList())) {
                ret.add(toInventoryEntry(entity));
            }
            return ret;
        } catch (MongoException e) {
            log.error("Can't find tokens with accid " + accountId, e);
            throw new MongoDaoException("Can't find tokens with accid " + accountId);
        }
    }

    /**
     * Returns the inventory entries of tokens of the given type, bound by limit and offset.
     *
     * @param datastore   datastore.
     * @param type        token type.
     * @param limit       maximum number of entries.
     * @param offset      number of entries to skip.
     * @param entityClass entity class.
     * @return the entries, never null.
     * @throws MongoDaoException if error.
     */
    public List<TokenInventoryEntry> getTokenInventoryByType(Datastore datastore, int type, int limit, int offset, Class<AuthTokenEntity> entityClass) throws MongoDaoException {
        try {
            List<TokenInventoryEntry> ret = new ArrayList<>();
            FindOptions options = new FindOptions().skip(offset).limit(limit);
            for (AuthTokenEntity entity : datastore.find(entityClass).filter(Filters.eq("type", type)).stream(options).collect(Collectors.toList())) {
                ret.add(toInventoryEntry(entity));
            }
            return ret;
        } catch (MongoException e) {
            log.error("Can't find tokens of type " + type, e);
            throw new MongoDaoException("Can't find tokens of type " + type);
        }
    }

    /**
     * Maps a stored token to an inventory entry. A zero timestamp means the value was never written and is
     * reported as unknown instead of as the epoch.
     *
     * @param entity the stored token.
     * @return the inventory entry.
     */
    private TokenInventoryEntry toInventoryEntry(AuthTokenEntity entity) {
        long created = entity.getDaoCreated() == 0 ? TokenInventoryEntry.TIMESTAMP_UNKNOWN : entity.getDaoCreated();
        long lastUsed = entity.getLastUsedAt() == 0 ? TokenInventoryEntry.TIMESTAMP_UNKNOWN : entity.getLastUsedAt();

        return new TokenInventoryEntry(new AccountId(entity.getAccid()), entity.getType(),
                TokenObfuscator.obfuscate(entity.getToken()), created, lastUsed, entity.getExpiryTimestamp(),
                entity.isMultiUse(), entity.isExclusive(), entity.isExclusiveInType());
    }
}
