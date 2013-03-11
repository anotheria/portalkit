package net.anotheria.portalkit.services.storage.inmemory.query.support;

import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.BetweenModifier;
import net.anotheria.portalkit.services.storage.query.BetweenQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link BetweenQuerySupport} test.
 * 
 * @author Alexandr Bolbat
 */
public class BetweenQuerySupportTest {

	/**
	 * Complex test.
	 */
	@Test
	public void complexTest() {
		BetweenQuery query = BetweenQuery.create("intValue", -1, 1);
		TestVO bean = new TestVO();
		bean.setIntValue(2);
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // more than
		bean.setIntValue(1);
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal max
		bean.setIntValue(0);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // between
		bean.setIntValue(-1);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal min
		bean.setIntValue(-2);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // less than

		query = BetweenQuery.create("intValue", -1, 1, BetweenModifier.INCLUDING);
		bean = new TestVO();
		bean.setIntValue(2);
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // more than
		bean.setIntValue(1);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal max
		bean.setIntValue(0);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // between
		bean.setIntValue(-1);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal min
		bean.setIntValue(-2);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // less than
	}

}
