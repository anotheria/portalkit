package net.anotheria.portalkit.services.relation;


import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * @author bvanchuhov
 */
public class RelationServiceFactory implements ServiceFactory<RelationService> {

    @Override
    public RelationService create() {
        return SpringHolder.get(RelationService.class);
    }
}
