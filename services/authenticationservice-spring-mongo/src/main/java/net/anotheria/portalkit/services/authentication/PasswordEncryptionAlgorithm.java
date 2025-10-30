package net.anotheria.portalkit.services.authentication;

/**
 * This interface defines the algorithm the authentication service can use for password encryption.
 *
 * @author lrosenberg
 * @since 12.12.12 23:18
 */
public interface PasswordEncryptionAlgorithm {

	/**
	 * Called after initialization and allows to make customization (for example set a key).
	 * @param key
	 */
	void customize(String key);

	/**
	 * Encrypt password.
	 * @param password
	 * @return
	 */
	String encryptPassword(String password);
}
