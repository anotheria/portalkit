package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.PasswordEncryptionAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.14 19:56
 */
public class SHA256AuthTokenEncryptionAlgorithm implements PasswordEncryptionAlgorithm {

	@Override
	public void customize(String key) {

	}

	@Override
	public String encryptPassword(String password) {
		return new String(DigestUtils.sha256Hex(password));
	}
}