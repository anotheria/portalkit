package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class ApprovingServiceSpringConfigurator {

	public static void configure(){
		SpringHolder.register(ApprovalService.class, new AnnotationConfigApplicationContext(ApprovalServiceConfiguration.class));
		MetaFactory.addFactoryClass(ApprovalService.class, Extension.LOCAL, ApprovalServiceFactory.class);
	}
}
