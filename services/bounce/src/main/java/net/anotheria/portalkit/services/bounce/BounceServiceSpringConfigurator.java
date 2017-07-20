package net.anotheria.portalkit.services.bounce;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author lrosenberg
 * @since 20.07.17 09:43
 */
public class BounceServiceSpringConfigurator {
	public static void configure(){
		SpringHolder.register(BounceService.class, new AnnotationConfigApplicationContext(BounceServiceConfiguration.class));
		MetaFactory.addFactoryClass(BounceService.class, Extension.LOCAL, BounceServiceFactory.class);
	}
}
