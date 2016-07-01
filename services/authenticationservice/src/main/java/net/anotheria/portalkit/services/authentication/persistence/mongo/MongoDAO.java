package net.anotheria.portalkit.services.authentication.persistence.mongo;

import net.anotheria.portalkit.services.common.persistence.mongo.MongoConnector;
import org.mongodb.morphia.Datastore;

import java.util.List;

public interface MongoDAO<T> {
    void createEntity(Datastore datastore, T entity) throws MongoDaoException;

    void updateEntityPassword(Datastore datastore, T entity, String newPassword) throws MongoDaoException;

    T getEntity(Datastore datastore, String id, Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAll(Datastore datastore, Class<? extends T> tClass) throws MongoDaoException;

    List<? extends T> getAllById(Datastore datastore, String id, Class<? extends T> tClass) throws MongoDaoException;

    void deleteEntity(Datastore datastore, String id, Class<? extends T> tClass) throws MongoDaoException;

    T getEntityByAccountId(Datastore datastore, String accountId, Class<? extends T> tClass) throws MongoDaoException;

}
