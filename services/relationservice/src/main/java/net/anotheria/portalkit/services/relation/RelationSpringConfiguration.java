package net.anotheria.portalkit.services.relation;

import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author bvanchuhov
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan(RelationSpringConfiguration.BASE_PACKAGE)
@Configuration
public class RelationSpringConfiguration extends JpaSpringConfiguration {

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.relation";

    @Override
    protected String getServiceName() {
        return "relation";
    }

    @Override
    protected String getBasePackage() {
        return BASE_PACKAGE;
    }
}
