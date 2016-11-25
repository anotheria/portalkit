package net.anotheria.portalkit.services.bounce;

import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring configuration for {@link BounceService}.
 *
 * @author Vlad Lukjanenko
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan("net.anotheria.portalkit.services.bounce")
@Configuration
public class BounceServiceConfiguration extends JpaSpringConfiguration {

    @Override
    protected String getServiceName() {
        return "bounce";
    }

    @Override
    protected String getBasePackage() {
        return BounceServiceConfiguration.class.getPackage().getName();
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
