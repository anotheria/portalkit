package net.anotheria.portalkit.services.foreignid;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.integrity.UserDataManagingServiceWithIntegrityCheck;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link ForeignIdService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class ForeignIdServiceFactory implements ServiceFactory<ForeignIdService> {
	@Override
	public ForeignIdService create() {
		return ServiceProxyUtil.createServiceProxy(ForeignIdService.class, new ForeignIdServiceImpl(), "service", "portal-kit", true, UserDataManagingServiceWithIntegrityCheck.class);
	}
}
