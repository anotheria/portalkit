package net.anotheria.portalkit.adminapi.rest;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.shared.APITierConfigurator;
import net.anotheria.portalkit.adminapi.biz.BusinessTierConfigurator;
import net.anotheria.portalkit.adminapi.biz.util.StartDistributeMeEventing;
import net.anotheria.portalkit.adminapi.shared.ASGTierConfigurator;
import org.configureme.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.Duration;
import java.time.Instant;

/**
 * Application context initializer.
 */
public class ContextInitializer implements ServletContextListener {

    /**
     * Application name.
     */
    public static final String APPLICATION_NAME = "<[ Portalkit ADMIN-API ]>";
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextInitializer.class.getName());

    /**
     * Context initialization actions.
     */
    public void contextInitialized(ServletContextEvent event) {
        LOGGER.info("--- " + APPLICATION_NAME + " --- APPLICATION INITIALIZATION: STARTED --- ");

        //start distributeme eventing for the api.
        String distributemePortAsString = event.getServletContext().getInitParameter("distributeMePort");
        try {
            int port = Integer.parseInt(distributemePortAsString);
            StartDistributeMeEventing.startDistributeMeEventing(port);
        } catch (Exception any) {
            LOGGER.error("Can not start distributeme eventing on port " + distributemePortAsString, any);
        } //end start distributeme

        Instant start = Instant.now();
        configureBusinessTier();
        LOGGER.info("configureBusinessTier time: " + (Duration.between(Instant.now(), start)));

        start = Instant.now();
        configureAPITier();
        LOGGER.info("configureAPITier time: " + (Duration.between(Instant.now(), start)));

        LOGGER.info("--- " + APPLICATION_NAME + " --- APPLICATION INITIALIZATION: FINISHED --- ");
        LOGGER.info("--- " + APPLICATION_NAME + " --- CURRENT CONFIGURATION ENVIRONMENT: " + ConfigurationManager.INSTANCE.getDefaultEnvironment().expandedStringForm() + " --- ");
    }

    /**
     * Context tear down actions.
     */
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("--- " + APPLICATION_NAME + " --- APPLICATION DESTROYED --- ");
    }

    /**
     * Configuring Business Tier.
     */
    private void configureBusinessTier() {
        // custom project services
        BusinessTierConfigurator.configure();
        ASGTierConfigurator.configure();
    }

    /**
     * Configuring API Tier.
     */
    private void configureAPITier() {
        APITierConfigurator.configure();
    }


}
