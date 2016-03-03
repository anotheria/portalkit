package net.anotheria.portalkit.services.match.conf;

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
    protected String getBasePackageName() {
        return BASE_PACKAGE;
    }

    @Override
    protected String getEntityPackagesToScan() {
        return BASE_PACKAGE + ".entity";
    }

    @Override
    protected String getJdbcConfigurationName() {
        return "pk-jdbc-match";
    }
}
