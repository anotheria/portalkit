package net.anotheria.portalkit.services.coin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * Configurator that allows distributeme to initialize the service.
 */
public class CoinServiceSpringConfigurator {
    public static void configure() {
        SpringHolder.register(CoinService.class, new AnnotationConfigApplicationContext(CoinServiceConfiguration.class));
        MetaFactory.addFactoryClass(CoinService.class, Extension.LOCAL, CoinServiceFactory.class);
    }
}
