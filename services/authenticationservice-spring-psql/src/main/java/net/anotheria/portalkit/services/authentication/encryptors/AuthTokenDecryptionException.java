package net.anotheria.portalkit.services.authentication.encryptors;

/**
 * Thrown if an encrypted auth token can not be decrypted, either because it is malformed, because it was encrypted
 * with a different phrase, or because it has been tampered with. It extends IllegalArgumentException because that
 * is what the previously existing algorithms happened to throw for a broken token, so existing callers keep working.
 *
 * The message never contains the offending token or any part of it, tokens are secrets and must not end up in logs.
 *
 * @author lrosenberg
 * @since 02.09.26 10:24
 */
public class AuthTokenDecryptionException extends IllegalArgumentException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5061171927348905622L;

	public AuthTokenDecryptionException(String message) {
		super(message);
	}

	public AuthTokenDecryptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
