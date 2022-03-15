package net.anotheria.portalkit.services.session.bean;

import java.io.Serializable;

public class SessionKey implements Serializable {

    /**
     * Generated default serialVersionUID.
     */
    private static final long serialVersionUID = -3426326602499374546L;

    /**
     * AccountId.
     */
    private String accountId;

    /**
     * Auth token
     */
    private String authToken;

    public SessionKey() {
    }

    public SessionKey(String accountId, String authToken) {
        this.accountId = accountId;
        this.authToken = authToken;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionKey that = (SessionKey) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
        return authToken != null ? authToken.equals(that.authToken) : that.authToken == null;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (authToken != null ? authToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SessionKey{" +
                "accountId='" + accountId + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
