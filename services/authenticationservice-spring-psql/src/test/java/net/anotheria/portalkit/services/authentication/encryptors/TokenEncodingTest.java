package net.anotheria.portalkit.services.authentication.encryptors;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the link safe encoding of token payloads.
 *
 * @author lrosenberg
 * @since 02.09.26 11:20
 */
public class TokenEncodingTest {

	@Test
	public void testRoundtrip() {
		Random rnd = new Random(42);
		for (int length = 1; length < 200; length++) {
			byte[] data = new byte[length];
			rnd.nextBytes(data);
			assertArrayEquals(data, TokenEncoding.decode(TokenEncoding.encode(data)));
		}
	}

	/**
	 * The encoding of arbitrary bytes must never contain a character which could break a link, a mail or a message,
	 * and it must never contain the padding character.
	 */
	@Test
	public void testEncodingIsAlwaysSafe() {
		Random rnd = new Random(23);
		for (int length = 1; length < 200; length++) {
			byte[] data = new byte[length];
			rnd.nextBytes(data);
			String encoded = TokenEncoding.encode(data);
			assertTrue(TokenEncoding.isSafe(encoded), "not link safe: " + encoded);
			assertFalse(encoded.contains("="), "padding is not url safe");
		}
	}

	@Test
	public void testUnsafeStringsAreDetected() {
		assertFalse(TokenEncoding.isSafe(null));
		assertFalse(TokenEncoding.isSafe(""));

		String[] unsafeCharacters = new String[]{
				//separators of the parameter string
				"&", ":",
				//characters with a meaning in urls
				"?", "#", "/", "+", "=", "%", "@",
				//characters which terminate an auto detected link in mail clients
				"\"", "'", "<", ">", "(", ")", ",", ";",
				//whitespace, a plain text mail may be wrapped at any of those
				" ", "\t", "\n", "\r",
				//non ascii, gets re-encoded or normalized on the way
				"\u00E4",
				//invisible characters, the worst of all because nobody sees them in a broken link
				"\u200B", "\u00A0", "\uFEFF", "\u00AD"
		};

		for (String unsafe : unsafeCharacters) {
			assertFalse(TokenEncoding.isSafe("abc" + unsafe + "def"), "must be rejected: " + unsafe);
		}
	}

	@Test
	public void testSafeStringsAreAccepted() {
		assertTrue(TokenEncoding.isSafe(TokenEncoding.SAFE_ALPHABET));
		assertTrue(TokenEncoding.isSafe(TokenEncoding.encode("some auth token payload".getBytes(StandardCharsets.UTF_8))));
	}
}
