package net.anotheria.portalkit.services.session.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.session.bean.Session;
import net.anotheria.portalkit.services.session.bean.attribute.Attribute;

import java.util.List;

public interface SessionPersistenceService extends Service {

    void saveSession(Session session) throws SessionPersistenceServiceException;

    List<Session> loadSessionsByAttribute(Attribute attribute) throws SessionPersistenceServiceException;

    Session loadSession(String authToken) throws SessionPersistenceServiceException;

    List<Session> loadSessions() throws SessionPersistenceServiceException;

    boolean deleteSession(String authToken) throws SessionPersistenceServiceException;

}
