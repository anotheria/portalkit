package net.anotheria.portalkit.services.authentication.encryptors;

import javax.crypto.spec.GCMParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Auth token encryption based on AES-256 in GCM mode. This is the recommended algorithm for tokens which are sent
 * out in email links: the token stays decryptable, manipulation of the token is detected, the encoded form only
 * uses link safe characters and the result is roughly a third shorter than the hex output of
 * {@link BlowfishAuthTokenEncryptionAlgorithm}.
 *
 * Configured with a single phrase, from which the 256 bit key is derived, see {@link PassphraseKeys}.
 *
 * A random 96 bit nonce is generated per token. The birthday bound of that nonce size allows for a very large
 * number of tokens per phrase before a repetition becomes likely, which is far beyond what an auth token phrase
 * will ever see. Should a phrase ever be used for a truly enormous amount of tokens, rotate it.
 *
 * @author lrosenberg
 * @since 02.09.26 10:40
 */
public class AesGcmAuthTokenEncryptionAlgorithm extends AbstractAeadAuthTokenEncryptionAlgorithm {

	/**
	 * Length of the nonce in bytes. 12 bytes is the size GCM is specified and optimized for.
	 */
	private static final int NONCE_LENGTH = 12;

	/**
	 * Length of the authentication tag in bits.
	 */
	private static final int TAG_LENGTH_IN_BITS = 128;

	@Override
	protected String getTransformation() {
		return "AES/GCM/NoPadding";
	}

	@Override
	protected String getKeyAlgorithm() {
		return "AES";
	}

	@Override
	protected int getNonceLength() {
		return NONCE_LENGTH;
	}

	@Override
	protected AlgorithmParameterSpec createParameterSpec(byte[] nonce) {
		return new GCMParameterSpec(TAG_LENGTH_IN_BITS, nonce);
	}
}
