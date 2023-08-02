package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.anoplass.api.APIFactory;

public class AdminAPIFactory implements APIFactory<AdminAPI> {

    @Override
    public AdminAPI createAPI() {
        return new AdminAPIImpl(false);
    }

    public static AdminAPI getForUnitTests() {
        return new AdminAPIImpl(true);
    }

}
