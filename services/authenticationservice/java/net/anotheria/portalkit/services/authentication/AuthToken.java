package net.anotheria.portalkit.services.authentication;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 13:24
 */
public class AuthToken {

	/**
	 * Used algorithm. Used for encoding of the string.
	 */
	static enum Algorithm{

	}

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
	 * This method should return an authentication code that is usable from external source (mail, cookie etc).
	 * @return
	 */
	public String getEncodedAuthString(){
		throw new AssertionError("Not yet implemented");
	}
}
