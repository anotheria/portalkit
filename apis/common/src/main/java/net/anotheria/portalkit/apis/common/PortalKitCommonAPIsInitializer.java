package net.anotheria.portalkit.apis.common;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.apis.common.accountsettings.AccountSettingsAPI;
import net.anotheria.portalkit.apis.common.accountsettings.AccountSettingsAPIFactory;

public class PortalKitCommonAPIsInitializer {
    public static void configureAPIFactores(){
        APIFinder.addAPIFactory(AccountSettingsAPI.class, new AccountSettingsAPIFactory() );
    }
}

