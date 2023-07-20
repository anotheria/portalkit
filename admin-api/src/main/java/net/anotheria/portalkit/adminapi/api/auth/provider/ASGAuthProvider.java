package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.anosite.cms.user.CMSUserManager;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthenticationProviderException;

public class ASGAuthProvider implements AuthProvider {

    private CMSUserManager userManager;

    public ASGAuthProvider() {
        this.userManager = CMSUserManager.getInstance();
    }

    @Override
    public void authenticate(String login, String password) throws AdminAuthenticationProviderException {
        boolean canLogin = userManager.canLoginUser(login, password);
        if (!canLogin) {
            throw new AdminAuthenticationProviderException("Authentication failed. Bad credentials");
        }
    }

    @Override
    public AuthProviderType getProviderType() {
        return AuthProviderType.ASG;
    }
}
