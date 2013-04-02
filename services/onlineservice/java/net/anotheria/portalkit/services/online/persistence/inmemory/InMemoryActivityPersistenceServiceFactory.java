package net.anotheria.portalkit.services.online.persistence.inmemory;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;

/**
 * JDBC persistence  based  factory for {@link ActivityPersistenceService}.
 *
 * @author h3llka
 */
public class InMemoryActivityPersistenceServiceFactory implements ServiceFactory<ActivityPersistenceService> {
    @Override
    public ActivityPersistenceService create() {
        return ServiceProxyUtil.createPersistenceServiceProxy(ActivityPersistenceService.class, new InMemoryActivityPersistenceServiceImpl());
    }
}
