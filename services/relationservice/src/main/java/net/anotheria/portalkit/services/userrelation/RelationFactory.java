package net.anotheria.portalkit.services.userrelation;


import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * @author bvanchuhov
 */
public class RelationFactory implements ServiceFactory<RelationService> {

    @Override
    public RelationService create() {
        return SpringHolder.get(RelationService.class);
    }
}
