package net.anotheria.portalkit.services.bounce;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * {@link BounceService} factory.
 *
 * @author Vlad Lukjanenko
 */
public class BounceServiceFactory implements ServiceFactory<BounceService> {

    @Override
    public BounceService create() {
        return SpringHolder.get(BounceService.class);
    }
}
