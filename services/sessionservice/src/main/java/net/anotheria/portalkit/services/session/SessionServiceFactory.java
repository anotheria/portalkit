package net.anotheria.portalkit.services.session;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

public class SessionServiceFactory implements ServiceFactory<SessionService> {

    @Override
    public SessionService create() {
        return ServiceProxyUtil.createServiceProxy(SessionService.class, new SessionServiceImpl(), "service", "portal-kit", true);
    }

}
