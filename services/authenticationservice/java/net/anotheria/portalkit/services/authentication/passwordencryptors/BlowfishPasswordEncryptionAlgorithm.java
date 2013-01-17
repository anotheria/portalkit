package net.anotheria.portalkit.services.authentication.passwordencryptors;

import net.anotheria.portalkit.services.authentication.PasswordEncryptionAlgorithm;
import net.anotheria.util.crypt.CryptTool;

/**
 * Password encryption based on blowfish. Easy to use but has a weakness, passwords are decryptable.
 *
 * @author lrosenberg
 * @since 12.12.12 23:21
 */
public class BlowfishPasswordEncryptionAlgorithm implements PasswordEncryptionAlgorithm {

	private volatile CryptTool cryptTool;

	@Override
	public String encryptPassword(String password) {
		if (cryptTool==null)
			throw new IllegalStateException("Uninitialzed call customize(key) first");
		return cryptTool.encryptToHex(password);
	}

	/**
	 * Decrypts a password, shouldn't be used.
	 * @param encryptedPassword
	 * @return
	 */
	/* test */String decryptPassword(String encryptedPassword) {
		if (cryptTool==null)
			throw new IllegalStateException("Uninitialzed call customize(key) first");
		return cryptTool.decryptFromHex(encryptedPassword);
	}

	@Override
	public void customize(String key) {
		cryptTool = new CryptTool(key);
	}
}


