package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.anoplass.api.APIException;

public class AdminAPIAuthenticationException extends APIException {

    private FailCause failCause;

    public AdminAPIAuthenticationException() {
    }

    public AdminAPIAuthenticationException(String message) {
        super(message);
    }

    public AdminAPIAuthenticationException(String message, FailCause failCause) {
        super(message);
        this.failCause = failCause;
    }

    public AdminAPIAuthenticationException(FailCause failCause) {
        this.failCause = failCause;
    }

    public AdminAPIAuthenticationException(String message, Exception cause) {
        super(message, cause);
    }

    public FailCause getFailCause() {
        return failCause;
    }

    public void setFailCause(FailCause failCause) {
        this.failCause = failCause;
    }

    /**
     * Authentication fail cause.
     */
    public enum FailCause {

        /**
         * Password doesn't match with user password.
         */
        PASSWORD_DOESNT_MATCH

    }
}
