package net.anotheria.portalkit.adminapi.rest.filter.auth;

import net.anotheria.portalkit.services.common.AccountId;

public class AuthResult {

    private AccountId accountId;
    private AuthError authError;

    public AuthResult(AccountId accountId) {
        this.accountId = accountId;
    }

    public AuthResult(AuthError authError) {
        this.authError = authError;
    }

    public boolean isAuthorized() {
        return authError == null;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public AuthError getAuthError() {
        return authError;
    }
}
