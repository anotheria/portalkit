package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.config.AuthenticationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigAuthProvider implements AuthProvider {

    private static final Logger log = LoggerFactory.getLogger(ConfigAuthProvider.class);

    private AuthenticationConfig config;

    public ConfigAuthProvider() {
        this.config = AuthenticationConfig.getInstance();
    }

    @Override
    public void authenticate(String login, String password) throws AdminAPIAuthenticationException {
        if (!login.equals(config.getLogin()) || !password.equals(config.getPassword())) {
            throw new AdminAPIAuthenticationException("Authentication failed. Bad credentials");
        }
    }

    @Override
    public AuthProviderType getProviderType() {
        return AuthProviderType.CONFIG;
    }
}
