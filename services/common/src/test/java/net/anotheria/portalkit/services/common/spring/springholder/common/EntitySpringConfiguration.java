package net.anotheria.portalkit.services.common.spring.springholder.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bvanchuhov
 */
@Configuration
public class EntitySpringConfiguration {

    @Autowired
    private BasicEntityConf conf;

    @Bean
    public String name() {
        return conf.getName();
    }
}
