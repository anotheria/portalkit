package net.anotheria.portalkit.services.storage.inmemory.query.support;

import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.MoreThanModifier;
import net.anotheria.portalkit.services.storage.query.MoreThanQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link MoreThanQuerySupport} test.
 * 
 * @author Alexandr Bolbat
 */
public class MoreThanQuerySupportTest {

	/**
	 * Complex test.
	 */
	@Test
	public void complexTest() {
		MoreThanQuery query = MoreThanQuery.create("intValue", 0);
		TestVO bean = new TestVO();
		bean.setIntValue(1);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // more than
		bean.setIntValue(0);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal
		bean.setIntValue(-1);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // less than

		query = MoreThanQuery.create("intValue", 0, MoreThanModifier.MORE_OR_EQUAL);
		bean = new TestVO();
		bean.setIntValue(1);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // more than
		bean.setIntValue(0);
		Assert.assertTrue("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // equal
		bean.setIntValue(-1);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean)); // less than
	}

}
