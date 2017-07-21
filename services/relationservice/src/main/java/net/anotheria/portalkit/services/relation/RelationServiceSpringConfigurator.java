package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class RelationServiceSpringConfigurator {

	public static void configure(){
		SpringHolder.register(RelationService.class, new AnnotationConfigApplicationContext(RelationSpringConfiguration.class));
		MetaFactory.addFactoryClass(RelationService.class, Extension.LOCAL, RelationServiceFactory.class);
	}
}
