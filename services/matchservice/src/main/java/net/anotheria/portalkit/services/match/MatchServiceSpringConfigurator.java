package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class MatchServiceSpringConfigurator {

	public static void configure(){
		SpringHolder.register(MatchService.class, new AnnotationConfigApplicationContext(MatchSpringConfiguration.class));
		MetaFactory.addFactoryClass(MatchService.class, Extension.LOCAL, MatchServiceFactory.class);
	}
}
