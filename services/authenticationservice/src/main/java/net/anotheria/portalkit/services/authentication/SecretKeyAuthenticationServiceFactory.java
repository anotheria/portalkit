package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.DeletionService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AuthenticationService} factory for secret key implementation.*
 */
public class SecretKeyAuthenticationServiceFactory implements ServiceFactory<AuthenticationService> {

    @Override
    public AuthenticationService create() {
        return ServiceProxyUtil.createServiceProxy(AuthenticationService.class, new SecretKeyAuthenticationServiceImpl(), "service", "portal-kit", true, DeletionService.class);
    }

}
