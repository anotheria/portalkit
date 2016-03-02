package net.anotheria.portalkit.services.common.spring.springholder.entity_b;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author bvanchuhov
 */
@Service
public class EntityBServiceImpl implements EntityBService {

    @Autowired
    @Qualifier("name")
    private String name;

    @Override
    public EntityB getEntityB() {
        return new EntityB(name);
    }
}
