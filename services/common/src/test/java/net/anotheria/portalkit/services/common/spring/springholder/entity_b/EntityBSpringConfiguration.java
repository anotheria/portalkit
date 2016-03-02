package net.anotheria.portalkit.services.common.spring.springholder.entity_b;

import net.anotheria.portalkit.services.common.spring.springholder.common.BasicEntityConf;
import net.anotheria.portalkit.services.common.spring.springholder.common.EntitySpringConfiguration;
import org.configureme.ConfigurationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author bvanchuhov
 */
@Import(EntitySpringConfiguration.class)
@Configuration
@ComponentScan("net.anotheria.portalkit.services.common.spring.springholder.entity_b")
public class EntityBSpringConfiguration {

    @Bean
    public BasicEntityConf entityConf() {
        BasicEntityConf conf = new BasicEntityConf();
        ConfigurationManager.INSTANCE.configureAs(conf, "entityB-conf");
        return conf;
    }
}
