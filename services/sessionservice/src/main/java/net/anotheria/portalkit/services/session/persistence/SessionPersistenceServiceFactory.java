package net.anotheria.portalkit.services.session.persistence;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class SessionPersistenceServiceFactory implements ServiceFactory<SessionPersistenceService> {

    @Override
    public SessionPersistenceService create() {
        return new SessionPersistenceServiceImpl();
    }

}
