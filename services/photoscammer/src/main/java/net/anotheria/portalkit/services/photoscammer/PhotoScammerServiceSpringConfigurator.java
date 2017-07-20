package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class PhotoScammerServiceSpringConfigurator {

	public static void configure(){
		SpringHolder.register(PhotoScammerService.class, new AnnotationConfigApplicationContext(PhotoScammerServiceConfiguration.class));
		MetaFactory.addFactoryClass(PhotoScammerService.class, Extension.LOCAL, PhotoScammerServiceFactory.class);
	}
}
