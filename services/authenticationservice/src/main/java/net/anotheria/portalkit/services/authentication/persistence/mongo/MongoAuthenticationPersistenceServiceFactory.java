package net.anotheria.portalkit.services.authentication.persistence.mongo;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseMongoPersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AuthenticationPersistenceService} factory for MongoDB implementation.
 * 
 * @author Stetsiuk Roman
 */
public class MongoAuthenticationPersistenceServiceFactory implements ServiceFactory<AuthenticationPersistenceService> {

	@Override
	public AuthenticationPersistenceService create() {
		return ServiceProxyUtil.createServiceProxy(AuthenticationPersistenceService.class, new MongoAuthenticationPersistenceServiceImpl(), "service",
				"portal-kit-persistence", true, BaseMongoPersistenceService.class);
	}

}
