package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptors;
import net.anotheria.util.crypt.CryptTool;

/**
 * Auth token encryption based on blowfish, the original algorithm of this package.
 *
 * The hex output is link safe, but it is twice as long as it needs to be and blowfish operates on 64 bit blocks
 * and without authentication, so a manipulated token is not detected as such. Kept for compatibility with tokens
 * which are already out there, for new setups prefer {@link AesGcmAuthTokenEncryptionAlgorithm}.
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


