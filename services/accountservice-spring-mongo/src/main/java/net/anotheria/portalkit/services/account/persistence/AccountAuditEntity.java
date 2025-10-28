package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.portalkit.services.account.AccountAudit;
import net.anotheria.portalkit.services.common.AccountId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pk-accounts-audit")
public class AccountAuditEntity {
    @Id
    private String id;
    /**
     * The account id of the user.
     */
    private String accountId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
