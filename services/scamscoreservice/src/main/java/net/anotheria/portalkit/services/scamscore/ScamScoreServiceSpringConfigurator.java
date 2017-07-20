package net.anotheria.portalkit.services.scamscore;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class ScamScoreServiceSpringConfigurator {

	public static void configure(){
		SpringHolder.register(ScamScoreService.class, new AnnotationConfigApplicationContext(ScamScoreServiceConfiguration.class));
		MetaFactory.addFactoryClass(ScamScoreService.class, Extension.LOCAL, ScamScoreServiceFactory.class);
	}
}
