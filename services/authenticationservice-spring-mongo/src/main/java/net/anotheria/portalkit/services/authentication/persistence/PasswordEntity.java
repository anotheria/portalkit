package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * PasswordEntity — persistence data object for user's passwords.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
@Document(collection = "pk-passwords")
public class PasswordEntity {
    /**
     * User's account id.
     */
    @Id
    private String accountId;
    /**
     * User's encrypted password.
     */
    private String password;

    /**
     * Default constructor.
     */
    public PasswordEntity() {}

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
