package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;

/**
 * Factory for auth providers
 */
public class AuthProviderFactory {

    private static final AdminAPIConfig CONFIG = AdminAPIConfig.getInstance();

    public static AuthProvider getAuthProvider() {
        switch (CONFIG.getAuthProvider()) {
            case ASG:
                return new ASGAuthProvider();
            case CONFIG:
                return new ConfigAuthProvider();
            default:
                throw new IllegalArgumentException("Cannot handle auth provider of: " + CONFIG.getAuthProvider());
        }
    }

}
