package net.anotheria.portalkit.services.userrelation;


import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * @author bvanchuhov
 */
public class UserRelationFactory implements ServiceFactory<UserRelationService> {

    @Override
    public UserRelationService create() {
        return SpringHolder.get(UserRelationService.class);
    }
}
