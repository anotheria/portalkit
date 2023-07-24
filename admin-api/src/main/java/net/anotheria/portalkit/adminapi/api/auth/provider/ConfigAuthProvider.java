package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.api.auth.AdminAuthenticationProviderException;
import net.anotheria.portalkit.adminapi.config.AuthenticationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auth provider that uses JSON config to perform authentication.
 */
public class ConfigAuthProvider implements AuthProvider {

    private static final Logger log = LoggerFactory.getLogger(ConfigAuthProvider.class);

    private AuthenticationConfig config;

    protected ConfigAuthProvider(boolean unitTests) {
    }

    public ConfigAuthProvider() {
        this.config = AuthenticationConfig.getInstance();
    }

    @Override
    public void authenticate(String login, String password) throws AdminAuthenticationProviderException {
        try {
            AuthenticationConfig.AccountConfig account = config.getAccountByLogin(login);
            if (account == null || (!login.equals(account.getLogin()) || !password.equals(account.getPassword()))) {
                throw new AdminAuthenticationProviderException("Authentication failed. Bad credentials");
            }
        } catch (AdminAuthenticationProviderException ex) {
            throw new AdminAuthenticationProviderException(ex.getMessage(), ex);
        }
    }

    @Override
    public AuthProviderType getProviderType() {
        return AuthProviderType.CONFIG;
    }
}
