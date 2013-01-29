package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 13:24
 */
public class AuthToken {


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
	 * The associated accountId.
	 */
	private AccountId accountId;

	/**
	 * Used internally to distinguish between generation of the code.
	 */
	private AuthTokenEncryptionAlgorithm algorithm;

	public AuthToken(){
		expiryTimestamp = Long.MAX_VALUE;
		multiUse = false;
		exclusive = false;
		exclusiveInType = false;
		type = AuthTokenTypes.APPLICATION_LOGIN;
	}

	/**
	 * This method should return an authentication code that is usable from external source (mail, cookie etc).
	 * @return
	 */
	public String getEncodedAuthString(){
		if (algorithm==null)
			throw new IllegalStateException("Can't encrypt auth token without algorithm");
		return algorithm.encryptAuthToken(this);
	}

	//we use deep equals method. Actually we only need equals for unit testing ;-)


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AuthToken authToken = (AuthToken) o;

		if (exclusive != authToken.exclusive) return false;
		if (exclusiveInType != authToken.exclusiveInType) return false;
		if (expiryTimestamp != authToken.expiryTimestamp) return false;
		if (multiUse != authToken.multiUse) return false;
		if (type != authToken.type) return false;
		if (accountId != null ? !accountId.equals(authToken.accountId) : authToken.accountId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (expiryTimestamp ^ (expiryTimestamp >>> 32));
		result = 31 * result + (multiUse ? 1 : 0);
		result = 31 * result + (exclusive ? 1 : 0);
		result = 31 * result + (exclusiveInType ? 1 : 0);
		result = 31 * result + type;
		result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
		return result;
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

	public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	public AuthTokenEncryptionAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AuthTokenEncryptionAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public boolean isExpired(){
		return expiryTimestamp<System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "AuthToken{" +
				"expiryTimestamp=" + expiryTimestamp +
				", multiUse=" + multiUse +
				", exclusive=" + exclusive +
				", exclusiveInType=" + exclusiveInType +
				", type=" + type +
				", accountId=" + accountId +
				", algorithm=" + algorithm +
				'}';
	}
}
