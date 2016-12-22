package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring configuration for {@link PhotoScammerService}.
 *
 * @author Vlad Lukjanenko
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan("net.anotheria.portalkit.services.photoscammer")
@Configuration
public class PhotoScammerServiceConfiguration extends JpaSpringConfiguration {

    @Override
    protected String getServiceName() {
        return "photoscammer";
    }

    @Override
    protected String getBasePackage() {
        return PhotoScammerServiceConfiguration.class.getPackage().getName();
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
