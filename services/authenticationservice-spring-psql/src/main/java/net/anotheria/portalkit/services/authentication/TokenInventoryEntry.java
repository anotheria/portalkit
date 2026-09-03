package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;

/**
 * One entry of the token inventory - what is known about a stored auth token without revealing the token itself.
 *
 * This is an inventory and not an audit trail: an entry exists exactly as long as the token it describes exists.
 * Deleting, consuming or replacing a token removes its entry, the same way a revoked personal access token
 * disappears from the list it was shown in.
 *
 * Entries never carry the token, only an obfuscated form of it, see {@link TokenObfuscator}. Two values can be
 * unknown and are reported as {@link #TIMESTAMP_UNKNOWN} resp. {@link #TYPE_UNKNOWN} rather than guessed: tokens
 * which were stored before the corresponding column existed have no creation timestamp, and no token has a last
 * used timestamp before it has been used for the first time after this feature was introduced.
 *
 * @author lrosenberg
 * @since 04.09.26 10:00
 */
public class TokenInventoryEntry implements Serializable {

	private static final long serialVersionUID = 6413409325741038391L;

	/**
	 * Value of {@link #getCreated()} and {@link #getLastUsed()} if the timestamp is not known.
	 */
	public static final long TIMESTAMP_UNKNOWN = -1L;

	/**
	 * Value of {@link #getType()} if the type is not known, which is the case for tokens stored before the type
	 * was persisted alongside them.
	 */
	public static final int TYPE_UNKNOWN = -1;

	/**
	 * Owner of the token.
	 */
	private final AccountId accountId;

	/**
	 * Type of the token, or {@link #TYPE_UNKNOWN}.
	 */
	private final int type;

	/**
	 * The token in a form which identifies it without being usable, see {@link TokenObfuscator}.
	 */
	private final String obfuscatedToken;

	/**
	 * When the token was created, or {@link #TIMESTAMP_UNKNOWN}.
	 */
	private final long created;

	/**
	 * When the token was last successfully authenticated with, or {@link #TIMESTAMP_UNKNOWN}. Kept at a coarse
	 * resolution on purpose, see AuthenticationServiceConfig#getLastUsedUpdateIntervalInHours().
	 */
	private final long lastUsed;

	/**
	 * When the token expires.
	 */
	private final long expiryTimestamp;

	/**
	 * If true the token can be used more than once.
	 */
	private final boolean multiUse;

	/**
	 * If true, creating this token erased all other tokens of its owner.
	 */
	private final boolean exclusive;

	/**
	 * If true, creating this token erased all other tokens of its owner which have the same type.
	 */
	private final boolean exclusiveInType;

	/**
	 * Creates a new entry.
	 *
	 * @param accountId       owner of the token.
	 * @param type            token type, or {@link #TYPE_UNKNOWN}.
	 * @param obfuscatedToken the obfuscated token, never the raw one.
	 * @param created         creation timestamp, or {@link #TIMESTAMP_UNKNOWN}.
	 * @param lastUsed        last used timestamp, or {@link #TIMESTAMP_UNKNOWN}.
	 * @param expiryTimestamp expiry timestamp of the token.
	 * @param multiUse        whether the token can be used more than once.
	 * @param exclusive       whether the token was exclusive on creation.
	 * @param exclusiveInType whether the token was exclusive in its type on creation.
	 */
	public TokenInventoryEntry(AccountId accountId, int type, String obfuscatedToken, long created, long lastUsed,
							   long expiryTimestamp, boolean multiUse, boolean exclusive, boolean exclusiveInType) {
		this.accountId = accountId;
		this.type = type;
		this.obfuscatedToken = obfuscatedToken;
		this.created = created;
		this.lastUsed = lastUsed;
		this.expiryTimestamp = expiryTimestamp;
		this.multiUse = multiUse;
		this.exclusive = exclusive;
		this.exclusiveInType = exclusiveInType;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public int getType() {
		return type;
	}

	public String getObfuscatedToken() {
		return obfuscatedToken;
	}

	public long getCreated() {
		return created;
	}

	public long getLastUsed() {
		return lastUsed;
	}

	public long getExpiryTimestamp() {
		return expiryTimestamp;
	}

	public boolean isMultiUse() {
		return multiUse;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public boolean isExclusiveInType() {
		return exclusiveInType;
	}

	/**
	 * Returns true if the creation timestamp of this token is known.
	 *
	 * @return true if {@link #getCreated()} carries a real timestamp.
	 */
	public boolean isCreatedKnown() {
		return created != TIMESTAMP_UNKNOWN;
	}

	/**
	 * Returns true if this token has been used at least once since last used tracking was introduced.
	 *
	 * @return true if {@link #getLastUsed()} carries a real timestamp.
	 */
	public boolean isUsed() {
		return lastUsed != TIMESTAMP_UNKNOWN;
	}

	/**
	 * Returns true if the token is expired.
	 *
	 * @return true if the expiry timestamp lies in the past.
	 */
	public boolean isExpired() {
		return expiryTimestamp < System.currentTimeMillis();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TokenInventoryEntry))
			return false;
		TokenInventoryEntry that = (TokenInventoryEntry) o;
		return type == that.type && created == that.created && lastUsed == that.lastUsed
				&& expiryTimestamp == that.expiryTimestamp && multiUse == that.multiUse
				&& exclusive == that.exclusive && exclusiveInType == that.exclusiveInType
				&& (accountId == null ? that.accountId == null : accountId.equals(that.accountId))
				&& (obfuscatedToken == null ? that.obfuscatedToken == null : obfuscatedToken.equals(that.obfuscatedToken));
	}

	@Override
	public int hashCode() {
		int result = accountId == null ? 0 : accountId.hashCode();
		result = 31 * result + type;
		result = 31 * result + (obfuscatedToken == null ? 0 : obfuscatedToken.hashCode());
		result = 31 * result + (int) (created ^ (created >>> 32));
		result = 31 * result + (int) (lastUsed ^ (lastUsed >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "TokenInventoryEntry{" +
				"accountId=" + accountId +
				", type=" + type +
				", obfuscatedToken='" + obfuscatedToken + '\'' +
				", created=" + created +
				", lastUsed=" + lastUsed +
				", expiryTimestamp=" + expiryTimestamp +
				", multiUse=" + multiUse +
				", exclusive=" + exclusive +
				", exclusiveInType=" + exclusiveInType +
				'}';
	}
}
