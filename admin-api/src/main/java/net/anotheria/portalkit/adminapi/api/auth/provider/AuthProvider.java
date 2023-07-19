package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthenticationProviderException;

public interface AuthProvider {

    void authenticate(String login, String password) throws AdminAPIAuthenticationException, AdminAuthenticationProviderException;

    AuthProviderType getProviderType();

}
