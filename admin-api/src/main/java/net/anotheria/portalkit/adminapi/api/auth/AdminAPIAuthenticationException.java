package net.anotheria.portalkit.adminapi.api.auth;

public class AdminAPIAuthenticationException extends Exception {

    public AdminAPIAuthenticationException() {
    }

    public AdminAPIAuthenticationException(String message) {
        super(message);
    }

    public AdminAPIAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
