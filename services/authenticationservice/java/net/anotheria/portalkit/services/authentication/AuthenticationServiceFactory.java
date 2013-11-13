package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AuthenticationService} factory for main implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AuthenticationServiceFactory implements ServiceFactory<AuthenticationService> {

	@Override
	public AuthenticationService create() {
		return ServiceProxyUtil.createServiceProxy(AuthenticationService.class, new AuthenticationServiceImpl(), "service", "portal-kit", true);
	}

}
