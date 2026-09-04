package net.anotheria.portalkit.services.authentication.encryptors;

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
	 * Decrypts what {@link #encryptPassword(String)} produced. Blowfish is a cipher and not a hash, which is the
	 * documented weakness of this algorithm for passwords - but it is exactly what makes it usable for the
	 * reversible account id mapping of SecretKeyAuthenticationServiceImpl, which has to get the real account id
	 * back to be able to report it.
	 *
	 * @param encryptedPassword the encrypted value.
	 * @return the decrypted value.
	 */
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


