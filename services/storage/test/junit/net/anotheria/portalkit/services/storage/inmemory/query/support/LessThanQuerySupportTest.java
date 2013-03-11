package net.anotheria.portalkit.services.storage.inmemory.query.support;

import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.LessThanModifier;
import net.anotheria.portalkit.services.storage.query.LessThanQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link LessThanQuerySupport} test.
 * 
 * @author Alexandr Bolbat
 */
public class LessThanQuerySupportTest {

	/**
	 * Complex test.
	 */
	@Test
	public void complexTest() {
		LessThanQuery query = LessThanQuery.create("intValue", 0);
		TestVO bean = new TestVO();
		bean.setIntValue(1);
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // more than
		bean.setIntValue(0);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal
		bean.setIntValue(-1);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // less than

		query = LessThanQuery.create("intValue", 0, LessThanModifier.LESS_OR_EQUAL);
		bean = new TestVO();
		bean.setIntValue(1);
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // more than
		bean.setIntValue(0);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal
		bean.setIntValue(-1);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // less than
	}

}
