package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptors;

/**
 * This
 *
 * @author lrosenberg
 * @since 28.01.13 15:07
 */
public class PlanAuthTokenEncryptionAlgorithm implements AuthTokenEncryptionAlgorithm {
	@Override
	public void customize(String key) {
	}

	@Override
	public String encryptAuthToken(AuthToken token) {
		String parameterString = AuthTokenEncryptors.toParameterString(token);
		return parameterString;
	}

	@Override
	public AuthToken decryptAuthToken(String encryptedVersion) {
		AuthToken ret = AuthTokenEncryptors.fromParameterString(encryptedVersion);
		return ret;
	}
}
