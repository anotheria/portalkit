package net.anotheria.portalkit.services.scamscore;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * {@link ScamScoreService} factory.
 *
 * @author Vlad Lukjanenko
 */
public class ScamScoreServiceFactory implements ServiceFactory<ScamScoreService> {

    @Override
    public ScamScoreService create() {
        return SpringHolder.get(ScamScoreService.class);
    }
}
