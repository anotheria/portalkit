package net.anotheria.portalkit.services.common.spring.springholder.entity_b;

import net.anotheria.portalkit.services.common.spring.springholder.BaseEntity;

/**
 * @author bvanchuhov
 */
public class EntityB extends BaseEntity {

    public EntityB(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityB{");
        sb.append(getName());
        sb.append('}');
        return sb.toString();
    }
}
