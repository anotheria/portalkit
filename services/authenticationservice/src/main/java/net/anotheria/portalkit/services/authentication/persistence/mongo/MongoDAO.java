package net.anotheria.portalkit.services.authentication.persistence.mongo;

import java.util.List;

public interface MongoDAO<T> {
    void createEntity(T entity) throws MongoDaoException;

    void updateEntityPassword(T entity, String newPassword) throws MongoDaoException;

    T getEntity(String id, Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAll(Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAllById(String id, Class<? extends T> tClass) throws MongoDaoException;

    void deleteEntity(String id, Class<? extends T> tClass) throws MongoDaoException;

    T getEntityByAccountId(String accountId, Class<? extends T> tClass) throws MongoDaoException;

}
