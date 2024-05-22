package net.anotheria.portalkit.services.account.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.mongo.entities.AccountEntity;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class MongoAccountDAOImpl implements MongoAccountDAO<BaseEntity> {

    private final Logger log;

    public MongoAccountDAOImpl() {
        log = LoggerFactory.getLogger(MongoAccountDAOImpl.class);
    }

    @Override
    public void createEntity(Datastore datastore, BaseEntity entity) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
            datastore.save(entity);
        } catch (DuplicateKeyException e) {
            log.error("id already exists " + entity);
            throw new MongoDaoException("Id already exists");
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't store " + entity.toString());
        }

    }

    @Override
    public void updateEntityFields(Datastore datastore, BaseEntity entity, Account newAccountData) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null.");
        }
        try {
            AccountEntity accountEntity = (AccountEntity)entity;
            accountEntity.setEmail(newAccountData.getEmail());
            accountEntity.setName(newAccountData.getName());
            accountEntity.setRegts(newAccountData.getRegistrationTimestamp());
            accountEntity.setStatus(newAccountData.getStatus());
            accountEntity.setTenant(newAccountData.getTenant());
            accountEntity.setType(newAccountData.getType());
            accountEntity.setBrand(newAccountData.getBrand());
            accountEntity.setDaoUpdated(System.currentTimeMillis());
            datastore.save(accountEntity);
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't update " + entity.toString());
        }
    }

    @Override
    public BaseEntity getEntity(Datastore datastore, String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            List<? extends BaseEntity> result = datastore.find(entityClass)
                    .filter(Filters.eq("accid", id))
                    .stream().collect(Collectors.toList());
            if (result.isEmpty()) {
                throw new MongoDaoException(entityClass.getSimpleName() + "with acid= " + id + " not found");
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with accid " + id);
        }
    }

    @Override
    public List<? extends BaseEntity> getAll(Datastore datastore, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        try {
            return datastore.find(entityClass).stream().collect(Collectors.toList());
        } catch (MongoException e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            throw new MongoDaoException("Can't delete " + entityClass.getSimpleName() + "with accid " + id);
        }
    }

    @Override
    public List<? extends BaseEntity> getAllById(Datastore datastore, String id, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null.");
        }
        try {
            return datastore.find(entityClass).filter(Filters.eq("externalId", id)).stream().collect(Collectors.toList());

        } catch (MongoException e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with accid " + accountId);
        }
    }

    @Override
    public BaseEntity getAccountByName(Datastore datastore, String name, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (name == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("name", name)).stream().collect(Collectors.toList());
            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with name " + name);
        }
    }

    @Override
    public BaseEntity getAccountByName(Datastore datastore, String name, String brand, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("Account name is empty");

        if (StringUtils.isEmpty(brand))
            throw new IllegalArgumentException("Account brand is empty");

        try {
            List<? extends BaseEntity> result =datastore.find(entityClass).filter(Filters.eq("brand", name), Filters.eq("brand", brand)).stream().collect(Collectors.toList());
            if (result.isEmpty())
                return null;

            return result.get(0);
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + " with name: " + name + " and brand: " + brand);
        }
    }

    @Override
    public BaseEntity getAccountByEmail(Datastore datastore, String email, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (email == null) {
            throw new IllegalArgumentException("AccountId is null.");
        }
        try {
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("email", email)).stream().collect(Collectors.toList());
            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "with email " + email);
        }
    }

    @Override
    public BaseEntity getAccountByEmail(Datastore datastore, String email, String brand, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (StringUtils.isEmpty(email))
            throw new IllegalArgumentException("Email is empty");

        if (StringUtils.isEmpty(brand))
            throw new IllegalArgumentException("Brand is empty");

        try {
            List<? extends BaseEntity> result = datastore.find(entityClass).filter(Filters.eq("email", email), Filters.eq("brand", brand)).stream().collect(Collectors.toList());
            if (result.isEmpty())
                return null;

            return result.get(0);
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + " with email: " + email + " and brand: " + brand);
        }
    }

    @Override
    public List<? extends BaseEntity> getAllAccounts(Datastore datastore, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        return null;
    }

    @Override
    public List<? extends BaseEntity> getAllAccounts(Datastore datastore, String brand, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        if (StringUtils.isEmpty(brand))
            throw new IllegalArgumentException("Brand is empty");

        try {
            return datastore.find(entityClass).filter(Filters.eq("brand", brand)).stream().collect(Collectors.toList());
        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find accounts with brand: " + brand + ". " + e.getMessage());
        }
    }

    @Override
    public List<? extends BaseEntity> getAccountsByType(Datastore datastore, int type, Class<? extends BaseEntity> entityClass) throws MongoDaoException {
        try {
            return datastore.find(entityClass).filter(Filters.eq("type", type)).stream().collect(Collectors.toList());

        } catch (MongoException e) {
            log.error(e.getMessage());
            throw new MongoDaoException("Can't find " + entityClass.getSimpleName() + "list by type");
        }
    }

}
