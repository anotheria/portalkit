package net.anotheria.portalkit.services.subscription;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * @author Vlad Lukjanenko
 */
public class SubscriptionServiceFactory implements ServiceFactory<SubscriptionService> {

    @Override
    public SubscriptionService create() {
        return SpringHolder.get(SubscriptionService.class);
    }
}

