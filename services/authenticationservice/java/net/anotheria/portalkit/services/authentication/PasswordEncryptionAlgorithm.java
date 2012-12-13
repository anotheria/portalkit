package net.anotheria.portalkit.services.authentication;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 23:18
 */
public interface PasswordEncryptionAlgorithm {

	void customize(String key);

	String encryptPassword(String password);

	String decryptPassword(String encryptedPassword);
}
