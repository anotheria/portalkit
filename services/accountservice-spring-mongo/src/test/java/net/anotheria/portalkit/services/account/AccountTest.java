package net.anotheria.portalkit.services.account;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for {@link Account}.
 *
 * @author lrosenberg
 * @since 16.01.13 09:36
 */
public class AccountTest {
	@Test
	public void testStatus() {
		Account acc = new Account();
		acc.addStatus(1);
		acc.addStatus(2);
		acc.addStatus(4);

		assertEquals(1 | 2 | 4, acc.getStatus());
		assertTrue(acc.hasStatus(1));
		assertTrue(acc.hasStatus(2));
		assertTrue(acc.hasStatus(4));
		assertFalse(acc.hasStatus(8));

		acc.removeStatus(2);
		acc.removeStatus(2);
		assertTrue(acc.hasStatus(1));
		assertFalse(acc.hasStatus(2));
		assertTrue(acc.hasStatus(4));

		acc.removeStatus(1);
		assertFalse(acc.hasStatus(1));
		assertFalse(acc.hasStatus(2));
		assertTrue(acc.hasStatus(4));

		acc.removeStatus(4);
		assertFalse(acc.hasStatus(1));
		assertFalse(acc.hasStatus(2));
		assertFalse(acc.hasStatus(4));

	}
}
