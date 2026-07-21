package net.anotheria.portalkit.services.authentication.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * JPA entity for the {@code auth_passwd} table (created by {@code V1_0__CreateTableForPasswords.sql}).
 * Column names match the lowercase identifiers the legacy migrations produced.
 */
@Entity
@Table(name = "auth_passwd")
public class PasswordEntity {

    /**
     * User's (encrypted) account id.
     */
    @Id
    @Column(name = "accid", length = 128)
    private String accountId;

    /**
     * User's encrypted password.
     */
    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "dao_created")
    private Long daoCreated;

    @Column(name = "dao_updated")
    private Long daoUpdated;

    public PasswordEntity() {}

    @PrePersist
    void onCreate() {
        if (daoCreated == null) {
            daoCreated = System.currentTimeMillis();
        }
    }

    @PreUpdate
    void onUpdate() {
        daoUpdated = System.currentTimeMillis();
    }

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

    public Long getDaoCreated() {
        return daoCreated;
    }

    public Long getDaoUpdated() {
        return daoUpdated;
    }
}
