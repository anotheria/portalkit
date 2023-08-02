package net.anotheria.portalkit.adminapi.shared;


import net.anotheria.access.AccessService;
import net.anotheria.access.impl.AccessServiceFactory;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anosite.gen.anoaccessapplicationdata.service.IAnoAccessApplicationDataService;
import net.anotheria.anosite.gen.anoaccessapplicationdata.service.fixture.AnoAccessApplicationDataServiceFixtureFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.asresourcedata.service.fixture.ASResourceDataServiceFixtureFactory;
import net.anotheria.anosite.gen.shared.service.MetaFactoryConfigurator;;

/**
 * ASG Tier configurator. Initializes ASG services.
 */
public class ASGTierConfigurator {

    /**
     * Private constructor.
     */
    private ASGTierConfigurator() {
        throw new IllegalAccessError("ASGTierConfigurator shouldn't be instantiated");
    }

    /**
     * Configures ASG tier.
     */
    public static void configure() {
        MetaFactoryConfigurator.configure();

        MetaFactory.addFactoryClass(IAnoAccessApplicationDataService.class, Extension.NONE, AnoAccessApplicationDataServiceFixtureFactory.class);
        MetaFactory.addFactoryClass(IAnoAccessApplicationDataService.class, Extension.DB, AnoAccessApplicationDataServiceFixtureFactory.class);
        MetaFactory.addFactoryClass(AccessService.class, Extension.NONE, AccessServiceFactory.class);
    }

}
