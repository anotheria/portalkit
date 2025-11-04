package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * AuthTokenEntity - persistence data object for user's authentication tokens.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
@Document(collection = "pk-auth-tokens")
public class AuthTokenEntity {
    /**
     * User's encrypted token.
     */
    @Id
    private String token;
    /**
     * User's account id.
     */
    private String accountId;
    /**
     * Timestamp at which this token expires.
     */
    private long expiryTimestamp;
    /**
     * If true the token can be used more than once.
     */
    private boolean multiUse;

    /**
     * If exclusive, creation of new token will lead to erasure of ALL other tokens.
     */
    private boolean exclusive;

    /**
     * If true, creation of new token of this type will lead to erasure of ALL tokens of the same type.
     */
    private boolean exclusiveInType;

    /**
     * Type of the token. Can be anything, a selection of types is offered in net.anotheria.portalkit.services.authentication.AuthTokenTypes.
     */
    private int type;

    /**
     * Default constructor.
     */
    public AuthTokenEntity() {}

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
}
