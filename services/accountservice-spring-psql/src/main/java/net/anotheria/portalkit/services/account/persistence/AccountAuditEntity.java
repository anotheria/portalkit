package net.anotheria.portalkit.services.account.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.anotheria.portalkit.services.account.AccountAudit;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * JPA entity for the {@code account_audit} table (created by {@code V1_0__CreateAccountAuditTable.sql}).
 * Mirrors the legacy {@code AccountAuditDAO}: the {@code created} value is stored in the {@code timestamp}
 * column.
 */
@Entity
@Table(name = "account_audit")
public class AccountAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The account id of the user.
     */
    @Column(name = "accountid", length = 256)
    private String accountId;

    /**
     * Sum of all values of old statuses for user.
     */
    @Column(name = "oldstatus")
    private long oldStatus;

    /**
     * Value of new status.
     */
    @Column(name = "newstatus")
    private long newStatus;

    /**
     * Value of status, which was removed.
     */
    @Column(name = "statusremoved")
    private long statusRemoved;

    /**
     * Value of status, which was added.
     */
    @Column(name = "statusadded")
    private long statusAdded;

    /**
     * Account audit creation time. Stored in the legacy {@code timestamp} column.
     */
    @Column(name = "\"timestamp\"")
    private long created = System.currentTimeMillis();

    public static AccountAuditEntity createFromAccountAudit(AccountAudit accountAudit) {
        AccountAuditEntity entity = new AccountAuditEntity();
        entity.setAccountId(accountAudit.getAccountId().getInternalId());
        entity.setOldStatus(accountAudit.getOldStatus());
        entity.setNewStatus(accountAudit.getNewStatus());
        entity.setStatusRemoved(accountAudit.getStatusRemoved());
        entity.setStatusAdded(accountAudit.getStatusAdded());
        entity.setCreated(accountAudit.getCreated());
        return entity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
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

    public AccountAudit toAccountAudit() {
        AccountAudit audit = new AccountAudit();
        audit.setId(id);
        audit.setAccountId(new AccountId(accountId));
        audit.setOldStatus(oldStatus);
        audit.setNewStatus(newStatus);
        audit.setStatusRemoved(statusRemoved);
        audit.setStatusAdded(statusAdded);
        audit.setCreated(created);
        return audit;
    }
}
