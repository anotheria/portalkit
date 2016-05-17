package net.anotheria.portalkit.services.userrelation;

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

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.userrelation";

    @Override
    protected String getServiceName() {
        return "userrelation";
    }

    @Override
    protected String getBasePackage() {
        return BASE_PACKAGE;
    }

    @Override
    protected String getDbConfigurationName() {
        return "pk-jdbc-userrelation";
    }
}
