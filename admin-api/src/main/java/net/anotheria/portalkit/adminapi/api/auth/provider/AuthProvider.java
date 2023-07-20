package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthenticationProviderException;

/**
 * Auth provider to perform authentication for admin-API.
 * See {@link AuthProviderType}
 */
public interface AuthProvider {

    /**
     * Performs user authentication by login in password.
     * If credentials are invalid, throws an exception.
     * If credentials are valid, then nothing.
     *
     * @param login    login
     * @param password password
     * @throws AdminAuthenticationProviderException in case of invalid credentials.
     */
    void authenticate(String login, String password) throws AdminAuthenticationProviderException;

    /**
     * Returns type of auth provider
     *
     * @return {@link AuthProviderType}
     */
    AuthProviderType getProviderType();

}
