package net.anotheria.portalkit.services.online;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link OnlineService} factory.
 *
 * @author h3llka
 */
public class OnlineServiceFactory implements ServiceFactory<OnlineService> {
    @Override
    public OnlineService create() {
        return new OnlineServiceImpl();
    }
}
