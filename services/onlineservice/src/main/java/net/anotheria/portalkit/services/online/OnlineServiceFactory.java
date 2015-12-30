package net.anotheria.portalkit.services.online;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link OnlineService} factory.
 *
 * @author h3llka
 */
public class OnlineServiceFactory implements ServiceFactory<OnlineService> {
    @Override
    public OnlineService create() {
		return ServiceProxyUtil.createServiceProxy(OnlineService.class, new OnlineServiceImpl(), "service", "portal-kit", true);
    }
}
