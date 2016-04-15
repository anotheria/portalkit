package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.spring.JpaSpringConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 23:43
 */
@Import(JpaSpringConfiguration.class)
@ComponentScan("net.anotheria.portalkit.services.subscription")
@Configuration
public class SubscriptionServiceConfiguration extends JpaSpringConfiguration{

	@Override
	protected String getServiceName() {
		return "subscription";
	}

	@Override
	protected String getBasePackage() {
		return SubscriptionServiceConfiguration.class.getPackage().getName();
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
