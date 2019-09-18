package net.anotheria.portalkit.services.coin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.anotheria.portalkit.services.common.flyway.FlywayUtils;
import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;

/**
 * Spring configuration for {@link CoinService}.
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan("net.anotheria.portalkit.services.coin")
@Configuration
public class CoinServiceConfiguration extends JpaSpringConfiguration {

    @Override
    protected String getServiceName() {
        return "coin";
    }

    @Override
    protected String getBasePackage() {
        return CoinServiceConfiguration.class.getPackage().getName();
    }

    @Override
    protected String[] getFlywayLocations() {
        return FlywayUtils.getDefaultFlywayLocations(getBasePackage() + ".persistence", getDbConfig().getDriver());
    }

    @Override
    protected String getEntityPackagesToScan() {
        return getBasePackage();
    }

    @Override
    protected String getDbConfigurationName() {
        return "pk-jdbc-" + getServiceName();
    }
}
