package net.anotheria.portalkit.services.storage.inmemory.query.support;

import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.common.QueryConstants;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link EqualQuerySupport} test.
 * 
 * @author Alexandr Bolbat
 */
public class EqualQuerySupportTest {

	/**
	 * Complex test.
	 */
	@Test
	public void complexTest() {
		EqualQuery query = EqualQuery.create("id", "123");

		TestVO bean = new TestVO("123");
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(query, bean));

		bean.setId("654");
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean));

		EqualQuery nestedQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "id", "abc");
		TestVO nestedBean = new TestVO("abc");
		bean.setSubObject(nestedBean);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(nestedQuery, bean));

		nestedBean.setId("zxc");
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(query, bean));

		nestedBean.setIntValue(567);
		nestedQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "intValue", 567);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(nestedQuery, bean));
		nestedQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "intValue", 876);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(nestedQuery, bean));

		nestedBean.setBooleanValue(true);
		nestedQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "booleanValue", true);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(nestedQuery, bean));
		nestedQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "booleanValue", false);
		Assert.assertFalse("Shouldn't pass", InMemoryQueryProcessor.executeFiltering(nestedQuery, bean));

		nestedBean.setBooleanValue(false);
		Boolean nullBoolean = null;
		nestedQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "booleanValue", nullBoolean);
		Assert.assertTrue("Should pass", InMemoryQueryProcessor.executeFiltering(nestedQuery, bean));
	}

}
