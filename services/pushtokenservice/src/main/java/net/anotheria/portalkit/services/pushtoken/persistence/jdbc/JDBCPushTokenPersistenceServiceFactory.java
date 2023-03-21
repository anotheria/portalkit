package net.anotheria.portalkit.services.pushtoken.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceService;

public class JDBCPushTokenPersistenceServiceFactory implements ServiceFactory<PushTokenPersistenceService> {

    @Override
    public PushTokenPersistenceService create() {
        return new JDBCPushTokenPersistenceServiceImpl();
    }
}
