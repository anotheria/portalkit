package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;

public interface AuthProvider {

    void authenticate(String login, String password) throws AdminAPIAuthenticationException;

    AuthProviderType getProviderType();

}
