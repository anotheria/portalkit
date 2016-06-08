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
@ComponentScan(SubscriptionServiceConfiguration.BASE_PACKAGE)
@Configuration
public class SubscriptionServiceConfiguration extends JpaSpringConfiguration{

    public static final String BASE_PACKAGE = "net.anotheria.portalkit.services.subscription";

	@Override
	protected String getServiceName() {
		return "subscription";
	}

	@Override
	protected String getBasePackage() {
        return BASE_PACKAGE;
    }
}
