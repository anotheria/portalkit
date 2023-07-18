package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.anoplass.api.APIFactory;

public class AdminAuthAPIFactory implements APIFactory<AdminAuthAPI> {

    @Override
    public AdminAuthAPI createAPI() {
        return new AdminAuthAPIImpl();
    }
}
