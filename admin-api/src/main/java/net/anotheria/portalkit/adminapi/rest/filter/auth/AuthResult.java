package net.anotheria.portalkit.adminapi.rest.filter.auth;

public class AuthResult {

    private String login;
    private AuthError authError;

    public AuthResult(String login) {
        this.login = login;
    }

    public AuthResult(AuthError authError) {
        this.authError = authError;
    }

    public boolean isAuthorized() {
        return authError == null;
    }

    public String getLogin() {
        return login;
    }

    public AuthError getAuthError() {
        return authError;
    }
}
