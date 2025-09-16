package net.anotheria.portalkit.apis.common.accountsettings;

import net.anotheria.anoplass.api.APIFactory;

public class AccountSettingsAPIFactory implements APIFactory<AccountSettingsAPI> {
    @Override
    public AccountSettingsAPI createAPI() {
        return new AccountSettingsAPIImpl();
    }
}
