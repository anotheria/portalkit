package net.anotheria.portalkit.services.authentication.passwordencryptors;

import net.anotheria.portalkit.services.authentication.PasswordEncryptionAlgorithm;
import net.anotheria.util.crypt.CryptTool;

/**
 * TODO comment this class
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

	@Override
	public String decryptPassword(String encryptedPassword) {
		if (cryptTool==null)
			throw new IllegalStateException("Uninitialzed call customize(key) first");
		return cryptTool.decryptFromHex(encryptedPassword);
	}

	@Override
	public void customize(String key) {
		cryptTool = new CryptTool(key);
	}
}


