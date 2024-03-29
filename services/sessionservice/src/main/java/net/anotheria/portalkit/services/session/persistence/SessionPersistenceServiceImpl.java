package net.anotheria.portalkit.services.session.persistence;

import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.session.bean.Session;
import net.anotheria.portalkit.services.session.bean.attribute.Attribute;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.mongo.GenericMongoServiceImpl;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;

import java.util.List;

public class SessionPersistenceServiceImpl extends GenericMongoServiceImpl<Session> implements SessionPersistenceService, EntityManagingService {

    /**
     * Storage configuration name.
     */
    private static final String MONGO_SERVICE_COLLECTION_CONFIG_NAME = "pk-storage-mongo-session";

    /**
     * Default constructor.
     */
    public SessionPersistenceServiceImpl() {
        super(Session.class, MONGO_SERVICE_COLLECTION_CONFIG_NAME, null, null, null);
        EntityManagingServices.createEntityCounter(this, "Sessions");
    }

    @Override
    public int getEntityCount(String s) {
        try {
            return Long.valueOf(countAll()).intValue();
        } catch (StorageException e) {
            return 0;
        }
    }

    @Override
    public void saveSession(Session session) throws SessionPersistenceServiceException {
        try {
            save(session);
        } catch (StorageException ex) {
            throw new SessionPersistenceServiceException("save(" + session + ") failed", ex);
        }
    }

    @Override
    public List<Session> loadSessionsByAttribute(Attribute attribute) throws SessionPersistenceServiceException {
        QueryBuilder builder = QueryBuilder.create();
        try {
            builder.add(CompositeQuery.create(EqualQuery.create(
                    "attributes." + attribute.getName() + ".value",
                    attribute.getValueAsString()))
            );
            return find(builder.build());
        } catch (StorageException ex) {
            throw new SessionPersistenceServiceException("find(" + builder + ") failed", ex);
        }
    }

    @Override
    public Session loadSession(String authToken) throws SessionPersistenceServiceException {
        QueryBuilder builder = QueryBuilder.create();
        try {
            builder.add(CompositeQuery.create(EqualQuery.create("key.authToken", authToken)));
            List<Session> sessions = find(builder.build());
            if (sessions.isEmpty()) {
                return null;
            }
            return sessions.get(0);
        } catch (StorageException ex) {
            throw new SessionPersistenceServiceException("find(" + builder + ") failed", ex);
        }
    }

    @Override
    public List<Session> loadSessions() throws SessionPersistenceServiceException {
        try {
            return findAll();
        } catch (StorageException ex) {
            throw new SessionPersistenceServiceException("findAll() failed", ex);
        }
    }

    @Override
    public boolean deleteSession(String authToken) throws SessionPersistenceServiceException {
        QueryBuilder builder = QueryBuilder.create();
        try {
            builder.add(CompositeQuery.create(EqualQuery.create("key.authToken", authToken)));
            delete(builder.build());
        } catch (StorageException ex) {
            throw new SessionPersistenceServiceException("deleteSession(" + authToken + ") failed", ex);
        }
        return true;
    }
}
