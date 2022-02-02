package net.anotheria.portalkit.services.session;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.session.bean.Session;
import net.anotheria.portalkit.services.session.bean.SessionNotFoundException;
import net.anotheria.portalkit.services.session.bean.SessionServiceException;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

@DistributeMe
@FailBy(strategyClass= RetryCallOnce.class)
public interface SessionService extends Service {

    void createSession(Session session) throws SessionServiceException;

    void updateSession(Session session) throws SessionServiceException;

    Session getSession(String authToken) throws SessionServiceException, SessionNotFoundException;

    List<Session> getSessions() throws SessionServiceException;

    boolean deleteSession(String authToken) throws SessionServiceException;

}
