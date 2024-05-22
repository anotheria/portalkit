package net.anotheria.portalkit.services.account.persistence.mongo;

import dev.morphia.Datastore;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;

import java.util.List;

public interface MongoAccountDAO<T> {
    void createEntity(Datastore datastore, T entity) throws MongoDaoException;

    void updateEntityFields(Datastore datastore, BaseEntity entity, Account newAccount) throws MongoDaoException;

    T getEntity(Datastore datastore, String id, Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAll(Datastore datastore, Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAllById(Datastore datastore, String id, Class<? extends T> tClass) throws MongoDaoException;

    void deleteEntity(Datastore datastore, String id, Class<? extends T> tClass) throws MongoDaoException;

    T getEntityByAccountId(Datastore datastore, String accountId, Class<? extends T> tClass) throws MongoDaoException;

    T getAccountByName(Datastore datastore, String name, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    T getAccountByName(Datastore datastore, String name, String brand, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    T getAccountByEmail(Datastore datastore, String email, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    T getAccountByEmail(Datastore datastore, String email, String brand, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    List<? extends T> getAllAccounts(Datastore datastore, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    List<? extends T> getAllAccounts(Datastore datastore, String brand, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    List<? extends T> getAccountsByType(Datastore datastore, int id, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

}
