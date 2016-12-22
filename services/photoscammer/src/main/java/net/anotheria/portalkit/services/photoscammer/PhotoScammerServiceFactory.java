package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * {@link PhotoScammerService} factory.
 *
 * @author Vlad Lukjanenko
 */
public class PhotoScammerServiceFactory implements ServiceFactory<PhotoScammerService> {

    @Override
    public PhotoScammerService create() {
        return SpringHolder.get(PhotoScammerService.class);
    }
}
