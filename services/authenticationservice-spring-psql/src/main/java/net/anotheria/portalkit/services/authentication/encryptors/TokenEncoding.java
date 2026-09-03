package net.anotheria.portalkit.services.authentication.encryptors;

import java.util.Base64;

/**
 * Encoding of the binary payload of an encrypted auth token into a string form which is safe to be carried
 * inside urls, emails and messages.
 *
 * Auth tokens end up in links which are sent to users via email or messenger. Such a link is passed through
 * mail transfer agents, html sanitizers, url auto-detection in messengers and finally through the query string
 * parsing of the receiving application. Therefore the encoded form must only use characters which survive all
 * of those steps unchanged, which means:
 * <ul>
 *     <li>no invisible or non-ascii characters (they can be re-encoded, stripped or normalized),</li>
 *     <li>no characters with a meaning in urls (&amp;, ?, #, /, =, %, +, space),</li>
 *     <li>no characters which terminate an auto-detected link in mail clients (quotes, brackets, comma).</li>
 * </ul>
 * The base64url alphabet (RFC 4648 section 5) without padding fulfills all of that, it consists of
 * A-Z, a-z, 0-9, '-' and '_' only.
 *
 * @author lrosenberg
 * @since 02.09.26 10:12
 */
public final class TokenEncoding {

	/**
	 * The alphabet an encoded token consists of. Every character in here is safe in a url path, a url query,
	 * a plain text email and a messenger message.
	 */
	public static final String SAFE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

	/**
	 * Encoder, base64url without padding ('=' is not url safe).
	 */
	private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

	/**
	 * Decoder, accepts the base64url alphabet with and without padding.
	 */
	private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

	/**
	 * Encodes the given bytes into a link safe string.
	 *
	 * @param data bytes to encode.
	 * @return link safe string representation.
	 */
	public static String encode(byte[] data) {
		return ENCODER.encodeToString(data);
	}

	/**
	 * Decodes a string previously created by {@link #encode(byte[])}.
	 *
	 * @param encoded encoded string.
	 * @return decoded bytes.
	 * @throws IllegalArgumentException if the string is not valid base64url.
	 */
	public static byte[] decode(String encoded) {
		return DECODER.decode(encoded);
	}

	/**
	 * Returns true if the given string consists exclusively of characters from the {@link #SAFE_ALPHABET}.
	 * Mainly useful for tests and for validating tokens produced by custom algorithms.
	 *
	 * @param candidate string to check.
	 * @return true if the string is safe to be embedded into a link.
	 */
	public static boolean isSafe(String candidate) {
		if (candidate == null || candidate.isEmpty())
			return false;
		for (int i = 0; i < candidate.length(); i++) {
			if (SAFE_ALPHABET.indexOf(candidate.charAt(i)) == -1)
				return false;
		}
		return true;
	}

	/**
	 * Prevent instantiation of this utility class.
	 */
	private TokenEncoding() {
	}
}
