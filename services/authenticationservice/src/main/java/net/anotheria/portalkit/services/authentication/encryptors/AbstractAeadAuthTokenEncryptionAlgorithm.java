package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptionAlgorithm;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptors;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Base class for auth token encryption algorithms which are built on an AEAD cipher (authenticated encryption with
 * associated data). Subclasses only name the cipher, everything else - key derivation, nonce handling, encoding and
 * error handling - happens here.
 *
 * Properties shared by all algorithms derived from this class:
 * <ul>
 *     <li>the token is encrypted, not hashed, so it stays decryptable and can carry the information of the
 *         {@link AuthToken},</li>
 *     <li>the ciphertext is authenticated, so a manipulated token is rejected instead of being decrypted into
 *         something arbitrary,</li>
 *     <li>a fresh random nonce is used per token, so encrypting the same token twice yields different strings and
 *         no information leaks from comparing two tokens,</li>
 *     <li>the result is encoded with {@link TokenEncoding} and therefore safe to be put into an email link,</li>
 *     <li>the algorithm is configured with exactly one phrase, like every other algorithm of this package.</li>
 * </ul>
 *
 * The binary layout before encoding is simply: nonce (nonceLength bytes) followed by ciphertext including the
 * authentication tag.
 *
 * @author lrosenberg
 * @since 02.09.26 10:31
 */
public abstract class AbstractAeadAuthTokenEncryptionAlgorithm implements AuthTokenEncryptionAlgorithm {

	/**
	 * Length of the derived key in bytes, 32 bytes are 256 bit which is what both supported ciphers want.
	 */
	protected static final int KEY_LENGTH_IN_BYTES = 32;

	/**
	 * Source of the nonces. SecureRandom is thread safe and shared, seeding it per instance would be wasteful
	 * since a new instance is created for every single encryption.
	 */
	private static final SecureRandom RND = new SecureRandom();

	/**
	 * The key derived from the configured phrase.
	 */
	private volatile SecretKey key;

	/**
	 * Returns the cipher transformation to be used, for example AES/GCM/NoPadding.
	 *
	 * @return name of the transformation as understood by {@link Cipher#getInstance(String)}.
	 */
	protected abstract String getTransformation();

	/**
	 * Returns the name of the key algorithm, for example AES. It is part of the key derivation, therefore two
	 * algorithms with a different key algorithm never share a key even if configured with the same phrase.
	 *
	 * @return name of the key algorithm.
	 */
	protected abstract String getKeyAlgorithm();

	/**
	 * Returns the length of the nonce in bytes.
	 *
	 * @return nonce length.
	 */
	protected abstract int getNonceLength();

	/**
	 * Wraps the given nonce into the parameter spec the cipher expects.
	 *
	 * @param nonce the freshly generated or the parsed nonce.
	 * @return parameter spec for {@link Cipher#init(int, java.security.Key, AlgorithmParameterSpec)}.
	 */
	protected abstract AlgorithmParameterSpec createParameterSpec(byte[] nonce);

	@Override
	public void customize(String key) {
		this.key = PassphraseKeys.deriveKey(key, KEY_LENGTH_IN_BYTES, getKeyAlgorithm());
	}

	@Override
	public String encryptAuthToken(AuthToken token) {
		SecretKey currentKey = getKey();
		String parameterString = AuthTokenEncryptors.toParameterString(token);

		byte[] nonce = new byte[getNonceLength()];
		RND.nextBytes(nonce);

		try {
			Cipher cipher = Cipher.getInstance(getTransformation());
			cipher.init(Cipher.ENCRYPT_MODE, currentKey, createParameterSpec(nonce));
			byte[] cipherText = cipher.doFinal(parameterString.getBytes(StandardCharsets.UTF_8));

			byte[] payload = new byte[nonce.length + cipherText.length];
			System.arraycopy(nonce, 0, payload, 0, nonce.length);
			System.arraycopy(cipherText, 0, payload, nonce.length, cipherText.length);

			return TokenEncoding.encode(payload);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Can't encrypt auth token with " + getTransformation(), e);
		}
	}

	@Override
	public AuthToken decryptAuthToken(String encryptedVersion) {
		SecretKey currentKey = getKey();
		if (encryptedVersion == null)
			throw new AuthTokenDecryptionException("Encrypted auth token is null");

		byte[] payload;
		try {
			payload = TokenEncoding.decode(encryptedVersion);
		} catch (IllegalArgumentException e) {
			throw new AuthTokenDecryptionException("Encrypted auth token is not properly encoded", e);
		}

		int nonceLength = getNonceLength();
		if (payload.length <= nonceLength)
			throw new AuthTokenDecryptionException("Encrypted auth token is too short to be valid");

		byte[] nonce = new byte[nonceLength];
		System.arraycopy(payload, 0, nonce, 0, nonceLength);

		try {
			Cipher cipher = Cipher.getInstance(getTransformation());
			cipher.init(Cipher.DECRYPT_MODE, currentKey, createParameterSpec(nonce));
			byte[] plain = cipher.doFinal(payload, nonceLength, payload.length - nonceLength);
			return AuthTokenEncryptors.fromParameterString(new String(plain, StandardCharsets.UTF_8));
		} catch (AEADBadTagException e) {
			throw new AuthTokenDecryptionException("Encrypted auth token is not authentic, it was tampered with or encrypted with another phrase", e);
		} catch (GeneralSecurityException e) {
			throw new AuthTokenDecryptionException("Can't decrypt auth token with " + getTransformation(), e);
		}
	}

	/**
	 * Returns the derived key and ensures the algorithm has been customized before use.
	 *
	 * @return the derived {@link SecretKey}.
	 */
	private SecretKey getKey() {
		SecretKey currentKey = key;
		if (currentKey == null)
			throw new IllegalStateException(getClass().getSimpleName() + " has not been customized with a phrase");
		return currentKey;
	}
}
