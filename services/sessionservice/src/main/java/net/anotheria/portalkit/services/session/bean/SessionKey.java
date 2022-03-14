package net.anotheria.portalkit.services.session.bean;

import net.anotheria.portalkit.services.common.AccountId;

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

    public SessionKey() {
    }

    public SessionKey(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionKey that = (SessionKey) o;

        return accountId != null ? accountId.equals(that.accountId) : that.accountId == null;
    }

    @Override
    public int hashCode() {
        return accountId != null ? accountId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SessionKey{" +
                "accountId='" + accountId + '\'' +
                '}';
    }
}
