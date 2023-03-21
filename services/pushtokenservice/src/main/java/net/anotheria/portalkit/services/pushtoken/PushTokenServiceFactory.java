package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

public class PushTokenServiceFactory implements ServiceFactory<PushTokenService> {

    @Override
    public PushTokenService create() {
        return ServiceProxyUtil.createServiceProxy(PushTokenService.class, new PushTokenServiceImpl(), "service", "portal-kit", true);
    }

}
