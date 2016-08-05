package net.anotheria.portalkit.services.scamscore;

import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring configuration for {@link ScamScoreService}.
 *
 * @author Vlad Lukjanenko
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan("net.anotheria.portalkit.services.scamscore")
@Configuration
public class ScamScoreServiceConfiguration extends JpaSpringConfiguration {

    @Override
    protected String getServiceName() {
        return "scamscore";
    }

    @Override
    protected String getBasePackage() {
        return ScamScoreServiceConfiguration.class.getPackage().getName();
    }

    @Override
    protected String getEntityPackagesToScan() {
        return getBasePackage();
    }

    @Override
    protected String getDbConfigurationName() {
        return "pk-jdbc-"+getServiceName();
    }
}
