package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.portalkit.services.common.flyway.FlywayUtils;
import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author akulyk
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan(PushTokenServiceConfiguration.BASE_PACKAGE)
@EnableJpaRepositories
@Configuration
public class PushTokenServiceConfiguration extends JpaSpringConfiguration {

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.pushtoken";

    @Override
    protected String getServiceName() {
        return "pushtoken";
    }

    @Override
    protected String getBasePackage() {
        return PushTokenServiceConfiguration.class.getPackage().getName();
    }

    @Override
    protected String[] getFlywayLocations() {
        return FlywayUtils.getDefaultFlywayLocations(getBasePackage() + ".persistence", getDbConfig().getDriver());
    }

    @Override
    protected String getDbConfigurationName() {
        return "pk-jdbc-" + getServiceName();
    }

}
