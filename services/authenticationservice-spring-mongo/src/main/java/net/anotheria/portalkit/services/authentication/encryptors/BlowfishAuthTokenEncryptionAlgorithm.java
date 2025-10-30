package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptors;
import net.anotheria.util.crypt.CryptTool;

/**
 * Password encryption based on blowfish. Easy to use but has a weakness, passwords are decryptable.
 *
 * @author lrosenberg
 * @since 12.12.12 23:21
 */
public class BlowfishAuthTokenEncryptionAlgorithm implements AuthTokenEncryptionAlgorithm {

	private volatile CryptTool cryptTool;

	@Override
	public void customize(String key) {
		cryptTool = new CryptTool(key);
	}

	@Override
	public String encryptAuthToken(AuthToken token) {
		String parameterString = AuthTokenEncryptors.toParameterString(token);
		return cryptTool.encryptToHex(parameterString);
	}

	@Override
	public AuthToken decryptAuthToken(String encryptedVersion) {
		String decryptedString = cryptTool.decryptFromHexTrim(encryptedVersion);
		return AuthTokenEncryptors.fromParameterString(decryptedString);
	}
}


