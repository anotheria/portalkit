package net.anotheria.portalkit.services.session.bean;

import java.io.Serializable;

public class SessionKey implements Serializable {

    /**
     * Generated default serialVersionUID.
     */
    private static final long serialVersionUID = -3426326602499374546L;

    /**
     * Auth token
     */
    private String authToken;

    public SessionKey() {
    }

    public SessionKey(String authToken) {
        this.authToken = authToken;
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

        return authToken != null ? authToken.equals(that.authToken) : that.authToken == null;
    }

    @Override
    public int hashCode() {
        return authToken != null ? authToken.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SessionKey{" +
                "authToken='" + authToken + '\'' +
                '}';
    }
}
