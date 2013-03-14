package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.util.Arrays;

import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.ContainsQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link ContainsQuerySupport} test.
 * 
 * @author Alexandr Bolbat
 */
public class ContainsQuerySupportTest {

	/**
	 * Complex test.
	 */
	@Test
	public void complexTest() {
		ContainsQuery query = ContainsQuery.create("intValue", 1, 3, 5);
		TestVO bean = new TestVO();
		bean.setIntValue(1);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
		bean.setIntValue(2);
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
		bean.setIntValue(3);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));

		query = ContainsQuery.create("intValues", 1, 3, 5);
		bean = new TestVO();
		bean.setIntValues(Arrays.asList(1, 2, 3, 4, 5));
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
		bean.setIntValues(Arrays.asList(6, 7, 8));
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
		bean.setIntValues(Arrays.asList(3, 9));
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));

		query = ContainsQuery.create("intValuesArray", 1, 3, 5);
		bean = new TestVO();
		bean.setIntValuesArray(new int[] { 1, 2, 3, 4, 5 });
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
		bean.setIntValuesArray(new int[] { 6, 7, 8 });
		Assert.assertFalse("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
		bean.setIntValuesArray(new int[] { 3, 9 });
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));
	}

}
