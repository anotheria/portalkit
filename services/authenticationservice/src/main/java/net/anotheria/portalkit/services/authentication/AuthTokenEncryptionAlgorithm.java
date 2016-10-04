package net.anotheria.portalkit.services.authentication;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.01.13 11:11
 */
public interface AuthTokenEncryptionAlgorithm {

	/**
	 * Called after initialization and allows to make customization (for example set a key).
	 * @param key	key.
	 */
	void customize(String key);

	/**
	 * Encrypt auth token.
	 * @param token	{@link AuthToken}
	 * @return	{@link String}
	 */
	String encryptAuthToken(AuthToken token);


	AuthToken decryptAuthToken(String encryptedVersion);

}
