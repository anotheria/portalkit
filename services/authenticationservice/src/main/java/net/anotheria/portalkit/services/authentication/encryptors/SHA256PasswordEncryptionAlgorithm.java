package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.PasswordEncryptionAlgorithm;
import net.anotheria.util.PasswordGenerator;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.14 19:56
 */
public class SHA256PasswordEncryptionAlgorithm implements PasswordEncryptionAlgorithm {

	private String myKey = PasswordGenerator.generate(10);

	@Override
	public void customize(String key) {
		myKey = key;
	}

	@Override
	public String encryptPassword(String password) {
		return new String(DigestUtils.sha256Hex(password+myKey));
	}
}