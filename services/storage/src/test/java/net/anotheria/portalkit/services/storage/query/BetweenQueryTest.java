package net.anotheria.portalkit.services.storage.query;

import junit.framework.Assert;

import org.junit.Test;

/**
 * {@link BetweenQueryTest} test.
 * 
 * @author Alexandr Bolbat
 */
public class BetweenQueryTest {

	/**
	 * Complex test.
	 */
	@Test
	public void complexTest() {
		BetweenQuery query = BetweenQuery.create("intValue", -1, 1);
		Assert.assertNotNull(query);
		Assert.assertEquals("intValue", query.getFieldName());
		Assert.assertNotNull(query.getModifier());
		Assert.assertEquals(BetweenModifier.DEFAULT, query.getModifier());
		Assert.assertNotNull(query.getPairValue());
		Assert.assertNotNull(query.getQueryValue());
		Assert.assertSame(query.getPairValue(), query.getQueryValue());
		Assert.assertNotNull(query.getPairValue().getFirstValue());
		Assert.assertNotNull(query.getPairValue().getFirstValue().getValue());
		Assert.assertEquals(-1, query.getPairValue().getFirstValue().getValue());
		Assert.assertNotNull(query.getPairValue().getSecondValue());
		Assert.assertNotNull(query.getPairValue().getFirstValue().getValue());
		Assert.assertEquals(1, query.getPairValue().getSecondValue().getValue());
	}

	/**
	 * Error cases test.
	 */
	@Test
	public void errorCasesTest() {
		try {
			BetweenQuery.create(null, -1, 1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("aFieldName"));
		}
		try {
			BetweenQuery.create("", -1, 1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("aFieldName"));
		}
		try {
			BetweenQuery.create("   ", -1, 1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("aFieldName"));
		}
		try {
			BetweenQuery.create("intValue", null, 1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("first"));
		}
		try {
			BetweenQuery.create("intValue", -1, null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("second"));
		}
		try {
			BetweenQuery.create("intValue", 1, -1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("firstValue"));
			Assert.assertTrue(e.getMessage().contains("can't be more than"));
			Assert.assertTrue(e.getMessage().contains("secondValue"));
		}
	}

}
