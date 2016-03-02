package net.anotheria.portalkit.services.common.spring.springholder.common;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.springframework.stereotype.Component;

/**
 * @author bvanchuhov
 */
@Component
@ConfigureMe
public class BasicEntityConf {

    @Configure
    private String name;

    public String getName() {
        return name;
    }

    public BasicEntityConf setName(String name) {
        this.name = name;
        return this;
    }
}
