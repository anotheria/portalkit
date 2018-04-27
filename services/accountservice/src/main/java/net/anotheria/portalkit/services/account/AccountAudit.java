package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of account audit, where stored account status changes.
 *
 * @author ykalapusha
 */
public class AccountAudit implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 6925071556148443628L;
    /**
     * Record id.
     */
    private long id;
    /**
     * The account id of the user.
     */
    private AccountId accountId;
    /**
     * Sum of all values of old statuses for user.
     */
    private long oldStatus;
    /**
     * Value of new status.
     */
    private long newStatus;
    /**
     * Value of status, which was removed.
     */
    private long statusRemoved;
    /**
     * Value of status, which was added.
     */
    private long statusAdded;
    /**
     * Account audit creation time.
     */
    private long created;

    /**
     * Default constructor.
     */
    public AccountAudit() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public long getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(long oldStatus) {
        this.oldStatus = oldStatus;
    }

    public long getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(long newStatus) {
        this.newStatus = newStatus;
    }

    public long getStatusRemoved() {
        return statusRemoved;
    }

    public void setStatusRemoved(long statusRemoved) {
        this.statusRemoved = statusRemoved;
    }

    public long getStatusAdded() {
        return statusAdded;
    }

    public void setStatusAdded(long statusAdded) {
        this.statusAdded = statusAdded;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountAudit)) return false;
        AccountAudit that = (AccountAudit) o;
        return oldStatus == that.oldStatus &&
                newStatus == that.newStatus &&
                statusRemoved == that.statusRemoved &&
                statusAdded == that.statusAdded &&
                created == that.created &&
                Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, oldStatus, newStatus, statusRemoved, statusAdded, created);
    }

    @Override
    public String toString() {
        return "AccountAudit{" +
                "accountId=" + accountId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", statusRemoved=" + statusRemoved +
                ", statusAdded=" + statusAdded +
                ", created=" + created +
                '}';
    }
}
