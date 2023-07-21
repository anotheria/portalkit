package net.anotheria.portalkit.adminapi.api.auth.provider;

/**
 * Admin-API authentication provider.
 */
public enum AuthProviderType {

    /**
     * Provider that uses credentials from ASG - UserManagement section
     */
    ASG,

    /**
     * Provider that uses credentials from JSON config (pk-admin-api-authentication-config.json)
     */
    CONFIG

}
