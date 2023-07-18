package net.anotheria.portalkit.adminapi.api.shared;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.admin.AdminAPI;
import net.anotheria.portalkit.adminapi.api.admin.AdminAPIFactory;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthAPI;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthAPIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API tier configurator.
 */
public final class APITierConfigurator {

    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(APITierConfigurator.class);

    /**
     * Default constructor.
     */
    private APITierConfigurator() {
        throw new IllegalAccessError();
    }

    public static void configure() {
        configureCommonParts();
    }

    /**
     * Configure API tier.
     */
    private static void configureCommonParts() {
        LOGGER.debug("configure() Configuring API tier: STARTED");

        APIFinder.addAPIFactory(AdminAPI.class, new AdminAPIFactory());
        APIFinder.addAPIFactory(AdminAuthAPI.class, new AdminAuthAPIFactory());

        LOGGER.debug("configure() Configuring API tier: FINISHED");
    }

}
