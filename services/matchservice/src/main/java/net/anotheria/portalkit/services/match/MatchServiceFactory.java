package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * @author bvanchuhov
 */
public class MatchServiceFactory implements ServiceFactory<MatchService> {

    @Override
    public MatchService create() {
        return SpringHolder.get(MatchService.class);
    }
}
