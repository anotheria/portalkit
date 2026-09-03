package net.anotheria.portalkit.services.authentication.encryptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Derives cipher keys from the single configuration phrase an auth token encryption algorithm is customized with.
 *
 * The configuration of an encryptor (see AuthTokenEncryptors.AuthenticationAlgorithmConfig) offers exactly one
 * string, the key aka phrase. That phrase is human chosen and of arbitrary length, whereas a block or stream
 * cipher needs a key of an exact length and of full entropy, so the phrase is stretched with PBKDF2.
 *
 * The salt is derived from a constant and from the name of the target key algorithm instead of being random,
 * because the derivation must be reproducible - the same phrase has to produce the same key in every jvm of the
 * cluster and after every restart, otherwise previously issued tokens would stop being decryptable. Including the
 * key algorithm in the salt gives domain separation, so the same phrase configured for two different algorithms
 * results in two unrelated keys.
 *
 * Since AuthTokenEncryptors creates a new algorithm instance and calls customize on it for every single encrypt
 * and decrypt call, the derived keys are cached, otherwise the iteration count would be paid per token.
 *
 * @author lrosenberg
 * @since 02.09.26 10:20
 */
public final class PassphraseKeys {

	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(PassphraseKeys.class);

	/**
	 * Key derivation function. Available in every jdk since 8.
	 */
	private static final String KDF_ALGORITHM = "PBKDF2WithHmacSHA256";

	/**
	 * Constant part of the salt. Changing this value invalidates all previously issued tokens.
	 */
	private static final String SALT_PREFIX = "net.anotheria.portalkit.authtoken.v1.";

	/**
	 * Iteration count of the key derivation. Paid once per distinct phrase thanks to the cache.
	 */
	private static final int ITERATIONS = 100000;

	/**
	 * Phrases shorter than this are accepted but logged as a warning, a short phrase is brute forceable.
	 */
	private static final int MIN_RECOMMENDED_PHRASE_LENGTH = 16;

	/**
	 * Cache of already derived keys, keyed by algorithm, key length and phrase.
	 */
	private static final Map<String, SecretKey> CACHE = new ConcurrentHashMap<String, SecretKey>();

	/**
	 * Derives a key of the given length for the given key algorithm from the given phrase.
	 *
	 * @param phrase           the configured phrase, must not be empty.
	 * @param keyLengthInBytes desired key length, for example 32 for a 256 bit key.
	 * @param keyAlgorithm     algorithm the resulting key is meant for, for example AES or ChaCha20.
	 * @return the derived {@link SecretKey}.
	 */
	public static SecretKey deriveKey(String phrase, int keyLengthInBytes, String keyAlgorithm) {
		if (phrase == null || phrase.isEmpty())
			throw new IllegalArgumentException("No phrase configured for " + keyAlgorithm + " auth token encryption");

		String cacheKey = keyAlgorithm + '/' + keyLengthInBytes + '/' + phrase;
		SecretKey cached = CACHE.get(cacheKey);
		if (cached != null)
			return cached;

		if (phrase.length() < MIN_RECOMMENDED_PHRASE_LENGTH) {
			log.warn("Configured phrase for " + keyAlgorithm + " auth token encryption is only " + phrase.length()
					+ " characters long, at least " + MIN_RECOMMENDED_PHRASE_LENGTH + " are recommended.");
		}

		SecretKey derived = derive(phrase, keyLengthInBytes, keyAlgorithm);
		CACHE.put(cacheKey, derived);
		return derived;
	}

	/**
	 * Performs the actual key stretching.
	 *
	 * @param phrase           the configured phrase.
	 * @param keyLengthInBytes desired key length.
	 * @param keyAlgorithm     algorithm the resulting key is meant for.
	 * @return the derived {@link SecretKey}.
	 */
	private static SecretKey derive(String phrase, int keyLengthInBytes, String keyAlgorithm) {
		byte[] salt = (SALT_PREFIX + keyAlgorithm).getBytes(StandardCharsets.UTF_8);
		PBEKeySpec spec = new PBEKeySpec(phrase.toCharArray(), salt, ITERATIONS, keyLengthInBytes * 8);
		try {
			byte[] keyBytes = SecretKeyFactory.getInstance(KDF_ALGORITHM).generateSecret(spec).getEncoded();
			return new SecretKeySpec(keyBytes, keyAlgorithm);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Can't derive a " + keyAlgorithm + " key with " + KDF_ALGORITHM, e);
		} finally {
			spec.clearPassword();
		}
	}

	/**
	 * Prevent instantiation of this utility class.
	 */
	private PassphraseKeys() {
	}
}
