package net.anotheria.portalkit.apis.common.authentication;

import net.anotheria.anoplass.api.APIFactory;

public class PortalKitAuthenticationAPIFactory implements APIFactory<PortalKitAuthenticationAPI> {
    @Override
    public PortalKitAuthenticationAPI createAPI() {
        return new PortalKitAuthenticationAPIImpl();
    }
}
