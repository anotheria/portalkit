package net.anotheria.portalkit.services.authentication.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * JPA entity for the {@code auth_token} table (created by {@code V1_1__CreateTableForTokens.sql} and
 * {@code V1_2__AddAdditionalFields.sql}). Column names match the lowercase identifiers the legacy
 * migrations produced.
 */
@Entity
@Table(name = "auth_token")
public class AuthTokenEntity {

    /**
     * User's encrypted token (primary key).
     */
    @Id
    @Column(name = "token", length = 512)
    private String token;

    /**
     * User's account id.
     */
    @Column(name = "accid", length = 128)
    private String accountId;

    /**
     * Timestamp at which this token expires.
     */
    @Column(name = "expirytimestamp")
    private long expiryTimestamp;

    /**
     * If true the token can be used more than once.
     */
    @Column(name = "multiuse")
    private boolean multiUse;

    /**
     * If exclusive, creation of new token will lead to erasure of ALL other tokens.
     */
    @Column(name = "exclusive")
    private boolean exclusive;

    /**
     * If true, creation of new token of this type will lead to erasure of ALL tokens of the same type.
     */
    @Column(name = "exclusiveintype")
    private boolean exclusiveInType;

    /**
     * Type of the token.
     */
    @Column(name = "type")
    private int type;

    /**
     * Timestamp of the last successful authentication with this token, added by V1_3__AddLastUsedAt.sql. Null
     * means the token has not been used since last used tracking was introduced, which is reported as unknown
     * and not as never used.
     */
    @Column(name = "last_used_at")
    private Long lastUsedAt;

    @Column(name = "dao_created")
    private Long daoCreated;

    @Column(name = "dao_updated")
    private Long daoUpdated;

    public AuthTokenEntity() {}

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public boolean isMultiUse() {
        return multiUse;
    }

    public void setMultiUse(boolean multiUse) {
        this.multiUse = multiUse;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isExclusiveInType() {
        return exclusiveInType;
    }

    public void setExclusiveInType(boolean exclusiveInType) {
        this.exclusiveInType = exclusiveInType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getDaoCreated() {
        return daoCreated;
    }

    public Long getDaoUpdated() {
        return daoUpdated;
    }

    public Long getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Long lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }
}
