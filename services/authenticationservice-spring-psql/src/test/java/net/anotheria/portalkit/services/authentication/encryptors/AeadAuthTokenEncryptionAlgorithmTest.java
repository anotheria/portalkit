package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptionAlgorithm;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the AEAD based auth token encryption algorithms. The properties tested here are the reason those
 * algorithms exist, so they are tested for both of them.
 *
 * @author lrosenberg
 * @since 02.09.26 11:02
 */
public class AeadAuthTokenEncryptionAlgorithmTest {

	/**
	 * A phrase as it would be configured in pk-auth.json.
	 */
	private static final String PHRASE = "PORTALKIT-FOR-AEAD-ENCRYPTION";

	@Test
	public void testAesGcmEncryptDecrypt() {
		testEncryptDecrypt(new AesGcmAuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testChaCha20EncryptDecrypt() {
		testEncryptDecrypt(new ChaCha20Poly1305AuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testAesGcmProducesLinkSafeTokens() {
		testLinkSafety(new AesGcmAuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testChaCha20ProducesLinkSafeTokens() {
		testLinkSafety(new ChaCha20Poly1305AuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testAesGcmIsRandomized() {
		testRandomization(new AesGcmAuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testChaCha20IsRandomized() {
		testRandomization(new ChaCha20Poly1305AuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testAesGcmDetectsManipulation() {
		testManipulationDetection(new AesGcmAuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testChaCha20DetectsManipulation() {
		testManipulationDetection(new ChaCha20Poly1305AuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testAesGcmRejectsWrongPhrase() {
		testWrongPhrase(new AesGcmAuthTokenEncryptionAlgorithm(), new AesGcmAuthTokenEncryptionAlgorithm());
	}

	@Test
	public void testChaCha20RejectsWrongPhrase() {
		testWrongPhrase(new ChaCha20Poly1305AuthTokenEncryptionAlgorithm(), new ChaCha20Poly1305AuthTokenEncryptionAlgorithm());
	}

	/**
	 * The same phrase configured for two different algorithms must not result in interchangeable tokens.
	 */
	@Test
	public void testAlgorithmsAreSeparated() {
		AesGcmAuthTokenEncryptionAlgorithm aes = new AesGcmAuthTokenEncryptionAlgorithm();
		aes.customize(PHRASE);
		ChaCha20Poly1305AuthTokenEncryptionAlgorithm chaCha = new ChaCha20Poly1305AuthTokenEncryptionAlgorithm();
		chaCha.customize(PHRASE);

		String encrypted = aes.encryptAuthToken(createToken());
		try {
			chaCha.decryptAuthToken(encrypted);
			fail("A token of another algorithm must not be decryptable");
		} catch (AuthTokenDecryptionException e) {
			//expected
		}
	}

	/**
	 * Tokens travel in email links, where every character counts, so the new algorithms must not be longer than
	 * the hex based blowfish they are meant to replace.
	 */
	@Test
	public void testTokensAreShorterThanBlowfish() {
		AuthToken token = createToken();

		BlowfishAuthTokenEncryptionAlgorithm blowfish = new BlowfishAuthTokenEncryptionAlgorithm();
		blowfish.customize(PHRASE);
		int blowfishLength = blowfish.encryptAuthToken(token).length();

		AesGcmAuthTokenEncryptionAlgorithm aes = new AesGcmAuthTokenEncryptionAlgorithm();
		aes.customize(PHRASE);
		assertTrue(aes.encryptAuthToken(token).length() < blowfishLength, "aes-gcm token should be shorter than the blowfish one");
	}

	@Test
	public void testUncustomizedAlgorithmFails() {
		try {
			new AesGcmAuthTokenEncryptionAlgorithm().encryptAuthToken(createToken());
			fail("An algorithm without a phrase must not encrypt");
		} catch (IllegalStateException e) {
			//expected
		}
	}

	@Test
	public void testEmptyPhraseIsRejected() {
		try {
			new AesGcmAuthTokenEncryptionAlgorithm().customize("");
			fail("An empty phrase must not be accepted");
		} catch (IllegalArgumentException e) {
			//expected
		}
	}

	@Test
	public void testGarbageIsRejected() {
		AesGcmAuthTokenEncryptionAlgorithm alg = new AesGcmAuthTokenEncryptionAlgorithm();
		alg.customize(PHRASE);

		try {
			alg.decryptAuthToken("not a token, and not even base64url");
			fail("Garbage must not be decryptable");
		} catch (AuthTokenDecryptionException e) {
			//expected
		}

		try {
			alg.decryptAuthToken("AAAA");
			fail("A too short token must not be decryptable");
		} catch (AuthTokenDecryptionException e) {
			//expected
		}
	}

	private void testEncryptDecrypt(AuthTokenEncryptionAlgorithm alg) {
		alg.customize(PHRASE);
		AuthToken token = createToken();

		AuthToken decrypted = alg.decryptAuthToken(alg.encryptAuthToken(token));

		assertEquals(token, decrypted);
	}

	private void testLinkSafety(AuthTokenEncryptionAlgorithm alg) {
		alg.customize(PHRASE);

		//one token is not enough, the encoding of a single random payload could be safe by accident.
		for (int i = 0; i < 100; i++) {
			String encrypted = alg.encryptAuthToken(createToken());
			assertTrue(TokenEncoding.isSafe(encrypted), "token contains a character which can break a link: " + encrypted);
		}
	}

	private void testRandomization(AuthTokenEncryptionAlgorithm alg) {
		alg.customize(PHRASE);
		AuthToken token = createToken();

		assertNotEquals(alg.encryptAuthToken(token), alg.encryptAuthToken(token),
				"encrypting the same token twice must not produce the same string");
	}

	private void testManipulationDetection(AuthTokenEncryptionAlgorithm alg) {
		alg.customize(PHRASE);
		String encrypted = alg.encryptAuthToken(createToken());

		String manipulated = manipulate(encrypted);
		assertFalse(encrypted.equals(manipulated));

		try {
			alg.decryptAuthToken(manipulated);
			fail("A manipulated token must not be decryptable");
		} catch (AuthTokenDecryptionException e) {
			//expected
		}
	}

	private void testWrongPhrase(AuthTokenEncryptionAlgorithm encrypting, AuthTokenEncryptionAlgorithm decrypting) {
		encrypting.customize(PHRASE);
		decrypting.customize(PHRASE + "-but-different");

		String encrypted = encrypting.encryptAuthToken(createToken());
		try {
			decrypting.decryptAuthToken(encrypted);
			fail("A token encrypted with another phrase must not be decryptable");
		} catch (AuthTokenDecryptionException e) {
			//expected
		}
	}

	/**
	 * Replaces one character in the middle of the given token by another one from the safe alphabet, simulating
	 * someone who fiddles with the token in the link.
	 *
	 * @param encrypted the encrypted token.
	 * @return the manipulated token, of the same length and still properly encoded.
	 */
	private String manipulate(String encrypted) {
		int position = encrypted.length() / 2;
		char original = encrypted.charAt(position);
		char replacement = original == 'A' ? 'B' : 'A';
		return encrypted.substring(0, position) + replacement + encrypted.substring(position + 1);
	}

	/**
	 * Creates a token as it would be used for an email link.
	 *
	 * @return a new {@link AuthToken}.
	 */
	private AuthToken createToken() {
		AuthToken token = new AuthToken();
		token.setAccountId(AccountId.generateNew());
		token.setType(42);
		token.setExclusive(true);
		token.setExclusiveInType(true);
		token.setMultiUse(false);
		token.setExpiryTimestamp(System.currentTimeMillis() + 1000L * 60 * 60 * 24);
		return token;
	}
}
