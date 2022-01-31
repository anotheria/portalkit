package net.anotheria.portalkit.services.session.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.session.bean.Session;

public interface SessionPersistenceService extends Service {

    void saveSession(Session session) throws SessionPersistenceServiceException;

    Session loadSession(String authToken) throws SessionPersistenceServiceException;

    boolean deleteSession(String authToken) throws SessionPersistenceServiceException;

}
