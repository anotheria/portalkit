package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;

/**
 * @author vkoulakov
 * @since 21.04.14 18:27
 */
public class ArchivedAccount extends Account implements Serializable, Cloneable {
    private static final long serialVersionUID = -7238185562478440815L;

    public long getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setDeletionTimestamp(long deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }

    public String getDeletionNote() {
        return deletionNote;
    }

    public void setDeletionNote(String deletionNote) {
        this.deletionNote = deletionNote;
    }

    /**
     * Deletion timestamp.
     */
    private long deletionTimestamp;
    /**
     * Note of the account deletion.
     */
    private String deletionNote;

    public ArchivedAccount() {
    }

    public ArchivedAccount(AccountId id) {
        super(id);
    }

    public ArchivedAccount(ArchivedAccount account){
        super(account);
    }

    @Override
    protected void copyFrom(Account anotherAccount) {
        super.copyFrom(anotherAccount);
        if (anotherAccount instanceof ArchivedAccount){
            this.deletionTimestamp = ((ArchivedAccount) anotherAccount).getDeletionTimestamp();
            this.deletionNote = ((ArchivedAccount) anotherAccount).getDeletionNote();
        }
    }
}
