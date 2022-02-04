package net.anotheria.portalkit.services.session;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.session.bean.Session;
import net.anotheria.portalkit.services.session.bean.SessionKey;
import net.anotheria.portalkit.services.session.bean.SessionNotFoundException;
import net.anotheria.portalkit.services.session.bean.SessionServiceException;
import net.anotheria.portalkit.services.session.persistence.SessionPersistenceService;
import net.anotheria.portalkit.services.session.persistence.SessionPersistenceServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SessionServiceImpl implements SessionService {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    /**
     * {@link SessionPersistenceService} instance.
     */
    private final SessionPersistenceService persistence;

    /**
     * Lock manager.
     */
    private final IdBasedLockManager<SessionKey> authTokenLockManager = new SafeIdBasedLockManager<SessionKey>();

    /**
     *
     */
    public SessionServiceImpl() {
        try {
            persistence = MetaFactory.get(SessionPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't instantiate persistence", e);
        }
    }

    @Override
    public void createSession(Session session) throws SessionServiceException {
        IdBasedLock<SessionKey> lock = authTokenLockManager.obtainLock(session.getKey());
        lock.lock();
        try {
            session.setCreationTimestamp(System.currentTimeMillis());
            persistence.saveSession(session);
        } catch (SessionPersistenceServiceException e) {
            throw new SessionServiceException("persistence.createSession failed", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateSession(Session session) throws SessionServiceException {
        IdBasedLock<SessionKey> lock = authTokenLockManager.obtainLock(session.getKey());
        lock.lock();
        try {
            Session toUpdate = persistence.loadSession(session.getKey().getAuthToken());
            toUpdate.setAttributes(session.getAttributes());
            toUpdate.setModifiedTimestamp(System.currentTimeMillis());
            persistence.saveSession(toUpdate);
        } catch (SessionPersistenceServiceException e) {
            throw new SessionServiceException("persistence.updateSession failed", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Session getSession(String authToken) throws SessionServiceException, SessionNotFoundException {
        IdBasedLock<SessionKey> lock = authTokenLockManager.obtainLock(new SessionKey(authToken));
        lock.lock();
        try {

            Session session = persistence.loadSession(authToken);
            if (session == null) {
                throw new SessionNotFoundException(authToken);
            }

            return session;
        } catch (SessionPersistenceServiceException e) {
            throw new SessionServiceException("persistence.loadSession failed", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Session> getSessions() throws SessionServiceException {
        try {
            return persistence.loadSessions();
        } catch (SessionPersistenceServiceException ex) {
            throw new SessionServiceException("persistence.loadSessions failed", ex);
        }
    }

    @Override
    public boolean deleteSession(String authToken) throws SessionServiceException {
        try {
            return persistence.deleteSession(authToken);
        } catch (SessionPersistenceServiceException e) {
            throw new SessionServiceException("persistence failed ", e);
        }
    }
}
