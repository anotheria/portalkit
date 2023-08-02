package net.anotheria.portalkit.adminapi.biz;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.adminapi.biz.util.DimeRegistryHelper;
import net.anotheria.portalkit.services.account.AccountAdminService;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.account.generated.RemoteAccountAdminServiceFactory;
import net.anotheria.portalkit.services.account.generated.RemoteAccountServiceFactory;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.generated.RemoteAccountSettingsServiceFactory;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.authentication.SecretKeyAuthenticationService;
import net.anotheria.portalkit.services.authentication.generated.RemoteAuthenticationServiceFactory;
import net.anotheria.portalkit.services.authentication.generated.RemoteSecretKeyAuthenticationServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business tier configurator.
 */
public final class BusinessTierConfigurator {

    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessTierConfigurator.class);

    /**
     * Default constructor.
     */
    private BusinessTierConfigurator() {
        throw new IllegalAccessError();
    }

    /**
     * Configure Business tier.
     */
    public static void configure() {
        LOGGER.info("configure() Configuring Business tier: STARTED");

        LOGGER.info("Configuring REMOTE services: STARTED");
        configureRemoteFactories();
        configureRemoteAliases();
        LOGGER.info("Configuring REMOTE services: END");

        LOGGER.info("configure() Configuring Business tier: FINISHED");
    }

    public static void configureRemotelyRunningServices() {
        // enable distributeMe eventing
        DimeRegistryHelper.configureRemoteServiceDimeEventing();

        LOGGER.info("Configuring REMOTE services: STARTED");
        configureRemoteFactories();
        configureRemoteAliases();
        LOGGER.info("Configuring REMOTE services: END");
    }

    private static void configureRemoteFactories() {
        MetaFactory.addFactoryClass(AccountService.class, Extension.REMOTE, RemoteAccountServiceFactory.class);
        MetaFactory.addFactoryClass(AccountAdminService.class, Extension.REMOTE, RemoteAccountAdminServiceFactory.class);
        MetaFactory.addFactoryClass(AccountSettingsService.class, Extension.REMOTE, RemoteAccountSettingsServiceFactory.class);
        MetaFactory.addFactoryClass(AuthenticationService.class, Extension.REMOTE, RemoteAuthenticationServiceFactory.class);
        MetaFactory.addFactoryClass(SecretKeyAuthenticationService.class, Extension.REMOTE, RemoteSecretKeyAuthenticationServiceFactory.class);
    }

    private static void configureRemoteAliases() {
        MetaFactory.addAlias(AccountService.class, Extension.REMOTE);
        MetaFactory.addAlias(AccountAdminService.class, Extension.REMOTE);
        MetaFactory.addAlias(AccountSettingsService.class, Extension.REMOTE);
        MetaFactory.addAlias(AuthenticationService.class, Extension.REMOTE);
        MetaFactory.addAlias(SecretKeyAuthenticationService.class, Extension.REMOTE);
    }
}
