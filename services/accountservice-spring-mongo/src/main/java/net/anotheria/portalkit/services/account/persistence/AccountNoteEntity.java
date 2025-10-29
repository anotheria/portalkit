package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.portalkit.services.account.AccountNote;
import net.anotheria.portalkit.services.common.AccountId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pk-accounts-notes")
public class AccountNoteEntity {
    @Id
    private String id;
    private long timestamp;
    private String author;
    private String text;
    private String accountId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
     * Creates a new instance, populated from the given AccountNote. Ignores any ids if set.
     * @param note
     * @return
     */
    public static AccountNoteEntity createFromAccountNote(AccountNote note) {
        AccountNoteEntity entity = new AccountNoteEntity();
        entity.setTimestamp(note.getTimestamp());
        entity.setAuthor(note.getAuthor());
        entity.setText(note.getText());
        entity.setAccountId(note.getAccountId().getInternalId());
        return entity;
    }

    public AccountNote toAccountNote(){
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
