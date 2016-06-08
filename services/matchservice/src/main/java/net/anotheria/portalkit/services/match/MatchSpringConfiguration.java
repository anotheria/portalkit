package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author bvanchuhov
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan(MatchSpringConfiguration.BASE_PACKAGE)
@Configuration
public class MatchSpringConfiguration extends JpaSpringConfiguration {

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.match";

    @Override
    protected String getServiceName() {
        return "match";
    }

    @Override
    protected String getBasePackage() {
        return BASE_PACKAGE;
    }
}
