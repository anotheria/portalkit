package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Configurator that allows distributeme to initialize the service.
 */
public class PushTokenSpringConfigurator {

    public static void configure() {
        SpringHolder.register(PushTokenService.class, new AnnotationConfigApplicationContext(PushTokenServiceConfiguration.class));
        MetaFactory.addFactoryClass(PushTokenService.class, Extension.LOCAL, PushTokenServiceFactory.class);
    }

}
