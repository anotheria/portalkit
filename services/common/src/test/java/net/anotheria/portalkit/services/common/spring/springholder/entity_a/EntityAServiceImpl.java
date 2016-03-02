package net.anotheria.portalkit.services.common.spring.springholder.entity_a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author bvanchuhov
 */
@Service
public class EntityAServiceImpl implements EntityAService {

    @Autowired
    @Qualifier("name")
    private String name;

    @Override
    public EntityA getEntityA() {
        return new EntityA(name);
    }
}
