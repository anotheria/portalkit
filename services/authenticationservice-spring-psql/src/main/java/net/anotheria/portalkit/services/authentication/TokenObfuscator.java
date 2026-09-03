package net.anotheria.portalkit.services.authentication;

/**
 * Turns an encrypted auth token into a form which can be shown in a token inventory without being usable.
 *
 * Obfuscation happens here and not in the consumers on purpose. A token is a credential, and if every project
 * masks its own, one of them will eventually get it wrong and write a live credential into a log or a support
 * ticket. Everything which leaves the service in a {@link TokenInventoryEntry} is obfuscated before it leaves.
 *
 * The obfuscated form keeps the last {@link #VISIBLE_CHARACTERS} characters and replaces everything before them
 * with {@link #MASK_CHARACTER}, keeping the original length. That is enough to tell two tokens of the same type
 * apart in a list, and far too little to reconstruct one - the tokens of the authenticated algorithms carry a
 * random nonce per token, so even two tokens with identical content differ over their whole length.
 *
 * @author lrosenberg
 * @since 04.09.26 10:00
 */
public final class TokenObfuscator {

	/**
	 * Number of characters at the end of the token which stay readable.
	 */
	public static final int VISIBLE_CHARACTERS = 4;

	/**
	 * Character every masked position is replaced with.
	 */
	public static final char MASK_CHARACTER = '*';

	/**
	 * Obfuscates the given token.
	 *
	 * A token which is not longer than {@link #VISIBLE_CHARACTERS} is masked completely - showing the tail of a
	 * token that short would show all of it.
	 *
	 * @param token the token to obfuscate, may be null.
	 * @return the obfuscated token, or null if the input was null.
	 */
	public static String obfuscate(String token) {
		if (token == null)
			return null;
		if (token.isEmpty())
			return token;

		int visible = token.length() > VISIBLE_CHARACTERS ? VISIBLE_CHARACTERS : 0;
		int masked = token.length() - visible;

		StringBuilder ret = new StringBuilder(token.length());
		for (int i = 0; i < masked; i++)
			ret.append(MASK_CHARACTER);
		ret.append(token.substring(masked));
		return ret.toString();
	}

	/**
	 * Prevent instantiation of this utility class.
	 */
	private TokenObfuscator() {
	}
}
