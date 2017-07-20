package net.anotheria.portalkit.services.subscription;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class SubscriptionServiceSpringConfigurator {

	public static void configure(){
		SpringHolder.register(SubscriptionService.class, new AnnotationConfigApplicationContext(SubscriptionServiceConfiguration.class));
		MetaFactory.addFactoryClass(SubscriptionService.class, Extension.LOCAL, SubscriptionServiceFactory.class);
	}
}
