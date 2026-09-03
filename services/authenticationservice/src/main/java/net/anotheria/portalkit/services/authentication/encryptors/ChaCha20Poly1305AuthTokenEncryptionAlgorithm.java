package net.anotheria.portalkit.services.authentication.encryptors;

import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Auth token encryption based on ChaCha20-Poly1305. An alternative to {@link AesGcmAuthTokenEncryptionAlgorithm}
 * with the same properties - decryptable, authenticated, link safe encoding, configured with a single phrase.
 *
 * Prefer this one over AES-GCM on hardware without AES instructions, where the software implementation of
 * ChaCha20 is the faster and the more side channel resistant of the two. On modern server cpus AES-GCM is
 * usually faster, so there this one is mainly a fallback if AES ever has to be avoided.
 *
 * @author lrosenberg
 * @since 02.09.26 10:44
 */
public class ChaCha20Poly1305AuthTokenEncryptionAlgorithm extends AbstractAeadAuthTokenEncryptionAlgorithm {

	/**
	 * Length of the nonce in bytes, fixed at 12 by the ChaCha20-Poly1305 specification.
	 */
	private static final int NONCE_LENGTH = 12;

	@Override
	protected String getTransformation() {
		return "ChaCha20-Poly1305";
	}

	@Override
	protected String getKeyAlgorithm() {
		return "ChaCha20";
	}

	@Override
	protected int getNonceLength() {
		return NONCE_LENGTH;
	}

	@Override
	protected AlgorithmParameterSpec createParameterSpec(byte[] nonce) {
		return new IvParameterSpec(nonce);
	}
}
