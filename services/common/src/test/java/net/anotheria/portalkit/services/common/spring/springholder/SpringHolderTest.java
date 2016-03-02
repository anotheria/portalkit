package net.anotheria.portalkit.services.common.spring.springholder;

import net.anotheria.portalkit.services.common.spring.SpringHolder;
import net.anotheria.portalkit.services.common.spring.springholder.entity_a.EntityA;
import net.anotheria.portalkit.services.common.spring.springholder.entity_a.EntityAService;
import net.anotheria.portalkit.services.common.spring.springholder.entity_a.EntityASpringConfiguration;
import net.anotheria.portalkit.services.common.spring.springholder.entity_b.EntityB;
import net.anotheria.portalkit.services.common.spring.springholder.entity_b.EntityBService;
import net.anotheria.portalkit.services.common.spring.springholder.entity_b.EntityBSpringConfiguration;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author bvanchuhov
 */
public class SpringHolderTest {

    @Test
    public void testSpringHolder() {
        SpringHolder.register(EntityAService.class, new AnnotationConfigApplicationContext(EntityASpringConfiguration.class));
        SpringHolder.register(EntityBService.class, new AnnotationConfigApplicationContext(EntityBSpringConfiguration.class));

        EntityAService entityAService = SpringHolder.get(EntityAService.class);
        EntityA entityA = entityAService.getEntityA();

        EntityBService entityBService =  SpringHolder.get(EntityBService.class);
        EntityB entityB = entityBService.getEntityB();

        assertThat(entityA.getName(), is("A"));
        assertThat(entityB.getName(), is("B"));
    }
}
