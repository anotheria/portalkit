package net.anotheria.portalkit.services.online.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;

/**
 * JDBC persistence based factory for {@link ActivityPersistenceService}.
 * 
 * @author h3llka
 */
public class JDBCActivityPersistenceServiceFactory implements ServiceFactory<ActivityPersistenceService> {
	@Override
	public ActivityPersistenceService create() {
		return ServiceProxyUtil.createPersistenceServiceProxy(ActivityPersistenceService.class, new JDBCActivityPersistenceServiceImpl(),
				BasePersistenceService.class);
	}
}
