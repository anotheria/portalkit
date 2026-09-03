package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.authentication.encryptors.TokenEncoding;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the dispatching of {@link AuthTokenEncryptors} over the algorithms configured in pk-auth.json.
 *
 * @author lrosenberg
 * @since 02.09.26 11:45
 */
public class AuthTokenEncryptorsTest {

	/**
	 * Encrypts with the configured default and decrypts again, the way the service does it.
	 */
	@Test
	public void testEncryptDecryptWithDefaultAlgorithm() {
		AuthToken token = createToken();

		String encrypted = AuthTokenEncryptors.encrypt(token);

		assertEquals(token, AuthTokenEncryptors.decrypt(encrypted));
	}

	/**
	 * The part of the encrypted token which the algorithm produces must be safe to be put into an email link.
	 * The shortcut and the ':' separator in front of it are added by {@link AuthTokenEncryptors} itself and are
	 * legal inside a url query as well.
	 */
	@Test
	public void testDefaultAlgorithmProducesALinkSafeToken() {
		String encrypted = AuthTokenEncryptors.encrypt(createToken());

		int separator = encrypted.indexOf(':');
		assertTrue("the encrypted token misses the algorithm shortcut", separator > 0);
		assertTrue("the algorithm shortcut is not link safe", TokenEncoding.isSafe(encrypted.substring(0, separator)));
		assertTrue("the encrypted payload is not link safe: " + encrypted, TokenEncoding.isSafe(encrypted.substring(separator + 1)));
	}

	/**
	 * Every algorithm which is configured must round trip, so that tokens issued by a previous default stay
	 * decryptable after the default has been switched.
	 */
	@Test
	public void testAllConfiguredAlgorithmsRoundtrip() {
		for (String shortcut : new String[]{"P", "B", "A", "C"}) {
			AuthToken token = createToken();
			AuthTokenEncryptionAlgorithm alg = AuthTokenEncryptors.getEncryptionAlgorithm(shortcut);

			String encrypted = alg.encryptAuthToken(token);

			assertEquals("algorithm " + shortcut + " does not round trip", token, AuthTokenEncryptors.decrypt(shortcut + ':' + encrypted));
		}
	}

	/**
	 * Creates a token as it would be used for an email link.
	 *
	 * @return a new {@link AuthToken}.
	 */
	private AuthToken createToken() {
		AuthToken token = new AuthToken();
		token.setAccountId(AccountId.generateNew());
		token.setType(AuthTokenTypes.MAIL_LOGIN);
		token.setMultiUse(false);
		token.setExpiryTimestamp(System.currentTimeMillis() + 1000L * 60 * 60 * 24);
		return token;
	}
}
