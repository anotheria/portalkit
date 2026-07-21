package net.anotheria.portalkit.services.account.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.anotheria.portalkit.services.account.AccountNote;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * JPA entity for the {@code account_note} table (created by {@code V1_11__CreateAccountNoteTable.sql}).
 * The primary key is a {@code BIGSERIAL}, therefore the id is a {@code long} matching the legacy
 * accountservice contract.
 */
@Entity
@Table(name = "account_note")
public class AccountNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "author", length = 64)
    private String author;

    @Column(name = "text")
    private String text;

    @Column(name = "accountid", length = 256)
    private String accountId;

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Creates a new instance, populated from the given AccountNote. Ignores any id if set.
     */
    public static AccountNoteEntity createFromAccountNote(AccountNote note) {
        AccountNoteEntity entity = new AccountNoteEntity();
        entity.setTimestamp(note.getTimestamp());
        entity.setAuthor(note.getAuthor());
        entity.setText(note.getText());
        entity.setAccountId(note.getAccountId().getInternalId());
        return entity;
    }

    public AccountNote toAccountNote() {
        AccountNote note = new AccountNote();
        note.setId(getId());
        note.setTimestamp(getTimestamp());
        note.setAuthor(getAuthor());
        note.setText(getText());
        note.setAccountId(new AccountId(getAccountId()));
        return note;
    }

    public void updateFromAccountNote(AccountNote accountNote) {
        this.timestamp = accountNote.getTimestamp();
        this.author = accountNote.getAuthor();
        this.text = accountNote.getText();
        this.accountId = accountNote.getAccountId().getInternalId();
    }
}
