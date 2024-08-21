package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.Objects;

public class AccountNote implements Serializable, Cloneable {
	private static final long serialVersionUID = -6849321598382243709L;

    private long id;
    private long timestamp = System.currentTimeMillis();
    private String author;
    private String text;
    private AccountId accountId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNote that = (AccountNote) o;
        return id == that.id && timestamp == that.timestamp && Objects.equals(author, that.author) && Objects.equals(text, that.text) && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, author, text, accountId);
    }

    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @Override
    protected AccountNote clone() {
        try {
            return AccountNote.class.cast(super.clone());
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Not cloneable? "+e.getMessage());
        }
    }
}
