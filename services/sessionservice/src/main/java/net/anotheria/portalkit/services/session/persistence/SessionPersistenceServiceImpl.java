package net.anotheria.portalkit.services.session.persistence;

import net.anotheria.portalkit.services.session.bean.Session;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.mongo.GenericMongoServiceImpl;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;

import java.util.List;

public class SessionPersistenceServiceImpl extends GenericMongoServiceImpl<Session> implements SessionPersistenceService {

    /**
     * Storage configuration name.
     */
    private static final String MONGO_SERVICE_COLLECTION_CONFIG_NAME = "pk-storage-mongo-session";

    /**
     * Default constructor.
     */
    public SessionPersistenceServiceImpl() {
        super(Session.class, MONGO_SERVICE_COLLECTION_CONFIG_NAME, null, null, null);
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
    public boolean deleteSession(String authToken) throws SessionPersistenceServiceException {
        try {
            return delete(authToken) != null;
        } catch (StorageException ex) {
            throw new SessionPersistenceServiceException("deleteDataspace(" + authToken + ") failed", ex);
        }
    }
}
