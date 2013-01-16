package net.anotheria.portalkit.services.account;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 16.01.13 09:36
 */
public class AccountTest {
	@Test public void testStatus(){
		Account acc = new Account();
		acc.addStatus(1);
		acc.addStatus(2);
		acc.addStatus(4);

		assertEquals(1 | 2 | 4, acc.getStatus());
		assertTrue(acc.hasStatus(1));
		assertTrue(acc.hasStatus(2));
		assertTrue(acc.hasStatus(4));
		assertFalse(acc.hasStatus(8));
	}
}
