package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

public class PushTokenServiceFactory implements ServiceFactory<PushTokenService> {

    @Override
    public PushTokenService create() {
        return SpringHolder.get(PushTokenService.class);
    }

}
