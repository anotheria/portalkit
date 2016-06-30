package net.anotheria.portalkit.services.account.persistence.mongo;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;

import java.util.List;

public interface MongoAccountDAO<T> {
    void createEntity(T entity) throws MongoDaoException;

    void updateEntityFields(BaseEntity entity, Account newAccount) throws MongoDaoException;

    T getEntity(String id, Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAll(Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAllById(String id, Class<? extends T> tClass) throws MongoDaoException;

    void deleteEntity(String id, Class<? extends T> tClass) throws MongoDaoException;

    T getEntityByAccountId(String accountId, Class<? extends T> tClass) throws MongoDaoException;


    T getAccountByName(String name, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    T getAccountByEmail(String email, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    List<? extends T> getAllAccounts(Class<? extends BaseEntity> entityClass) throws MongoDaoException;

    List<? extends T> getAccountsByType(int id, Class<? extends BaseEntity> entityClass) throws MongoDaoException;

}
