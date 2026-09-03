package net.anotheria.portalkit.services.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the obfuscation of tokens for the token inventory. The point of these tests is that nothing usable
 * survives obfuscation while two tokens still stay distinguishable.
 *
 * @author lrosenberg
 * @since 04.09.26 10:00
 */
public class TokenObfuscatorTest {

	@Test
	public void keepsTheLastFourCharactersOnly() {
		assertEquals("**********wxyz", TokenObfuscator.obfuscate("abcdefghijwxyz"));
	}

	@Test
	public void keepsTheOriginalLength() {
		String token = "A:0123456789abcdefghijklmnopqrstuvwxyz";
		assertEquals(token.length(), TokenObfuscator.obfuscate(token).length());
	}

	@Test
	public void masksShortTokensCompletely() {
		//showing the last four characters of a token of four characters would show all of it.
		assertEquals("****", TokenObfuscator.obfuscate("abcd"));
		assertEquals("**", TokenObfuscator.obfuscate("ab"));
	}

	@Test
	public void doesNotLeakTheTokenItself() {
		String token = "A:8Wm5cGdNRR3jHhV0rgPUn3bLQlKKm1IuLXCXjpVWkI4";
		String obfuscated = TokenObfuscator.obfuscate(token);

		assertFalse(token.equals(obfuscated), "the obfuscated form must not be the token");
		assertFalse(obfuscated.startsWith("A:"), "the algorithm prefix must not stay readable");
		assertTrue(obfuscated.startsWith("****"), "everything but the tail is masked");
	}

	@Test
	public void tellsTwoTokensApart() {
		//two tokens of the same account and type differ over their whole length because of the random nonce,
		//so the visible tail is enough to distinguish them in an inventory listing.
		String first = TokenObfuscator.obfuscate("A:aaaaaaaaaaaaaaaa1234");
		String second = TokenObfuscator.obfuscate("A:aaaaaaaaaaaaaaaa5678");
		assertFalse(first.equals(second));
	}

	@Test
	public void handlesNullAndEmpty() {
		assertNull(TokenObfuscator.obfuscate(null));
		assertEquals("", TokenObfuscator.obfuscate(""));
	}
}
