package net.anotheria.portalkit.services.common.spring.springholder.entity_a;

import net.anotheria.portalkit.services.common.spring.springholder.BaseEntity;

/**
 * @author bvanchuhov
 */
public class EntityA extends BaseEntity {

    public EntityA(String name) {
        super(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityA{");
        sb.append(getName());
        sb.append('}');
        return sb.toString();
    }
}
