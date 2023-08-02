package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.anoplass.api.APIException;

public class AdminAuthenticationProviderException extends APIException {

    public AdminAuthenticationProviderException() {
    }

    public AdminAuthenticationProviderException(String message) {
        super(message);
    }

    public AdminAuthenticationProviderException(String message, Exception cause) {
        super(message, cause);
    }
}
