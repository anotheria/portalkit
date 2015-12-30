package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.BetweenQuery;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.LessThanModifier;
import net.anotheria.portalkit.services.storage.query.LessThanQuery;
import net.anotheria.portalkit.services.storage.query.MoreThanQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;
import net.anotheria.portalkit.services.storage.query.common.QueryConstants;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Test;

/**
 * {@link CompositeQuerySupport} test.
 * 
 * @author Alexandr Bolbat
 */
public class CompositeQuerySupportTest {

	/**
	 * Complex test with flat query.
	 */
	@Test
	public void complexFlatTest() {
		List<TestVO> testingData = new ArrayList<TestVO>();
		for (int i = 0; i < 15; i++) {
			TestVO testObject = new TestVO(String.valueOf(i));
			testObject.setIntValue(i);
			if (i <= 5 && i >= 10)
				testObject.setBooleanValue(true);
			testingData.add(testObject);
		}

		BetweenQuery betweenQuery = BetweenQuery.create("intValue", 5, 10);
		EqualQuery equalQuery = EqualQuery.create("booleanValue", false);
		Query query = QueryBuilder.create(CompositeModifier.AND).add(betweenQuery).add(equalQuery).build();

		List<TestVO> filteringResult = InMemoryQueryProcessor.execute(testingData, query);
		Assert.assertNotNull(filteringResult);
		Assert.assertEquals(4, filteringResult.size());

		testingData.get(6).setBooleanValue(true);

		filteringResult = InMemoryQueryProcessor.execute(testingData, query);
		Assert.assertNotNull(filteringResult);
		Assert.assertEquals(3, filteringResult.size());

		TestVO subObject = new TestVO();
		subObject.setDoubleValue(5);
		testingData.get(7).setSubObject(subObject);
		subObject = new TestVO();
		subObject.setDoubleValue(10);
		testingData.get(8).setSubObject(subObject);
		subObject = new TestVO();
		subObject.setDoubleValue(15);
		testingData.get(9).setSubObject(subObject);

		LessThanQuery lessThanQuery = LessThanQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "doubleValue", new Double(10),
				LessThanModifier.LESS_OR_EQUAL);
		query = QueryBuilder.create(CompositeModifier.AND).add(betweenQuery).add(equalQuery).add(lessThanQuery).build();

		filteringResult = InMemoryQueryProcessor.execute(testingData, query);
		Assert.assertNotNull(filteringResult);
		Assert.assertEquals(2, filteringResult.size());

		testingData.get(7).getSubObject().setFloatValue(50);
		testingData.get(8).getSubObject().setFloatValue(100);

		MoreThanQuery moreThanQuery = MoreThanQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "floatValue", new Float(75));
		query = QueryBuilder.create(CompositeModifier.AND).add(betweenQuery).add(equalQuery).add(lessThanQuery).add(moreThanQuery).build();

		filteringResult = InMemoryQueryProcessor.execute(testingData, query);
		Assert.assertNotNull(filteringResult);
		Assert.assertEquals(1, filteringResult.size());
	}

	/**
	 * Complex test with nested queries.
	 */
	@Test
	public void complexNestedTest() {
		MoreThanQuery moreThanQuery = MoreThanQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "intValue", 10);
		LessThanQuery lessThanQuery = LessThanQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "intValue", 20);
		CompositeQuery subNestedComposite = CompositeQuery.create(CompositeModifier.AND, moreThanQuery, lessThanQuery);

		EqualQuery nestedEqualQuery = EqualQuery.create("subObject" + QueryConstants.QUERY_NESTING_SEPARATOR + "id", "allowed");
		CompositeQuery nestedComposite = CompositeQuery.create(CompositeModifier.OR, nestedEqualQuery, subNestedComposite);

		EqualQuery equalQuery = EqualQuery.create("id", "filledId");
		EqualQuery equalQuery2 = EqualQuery.create("id", "force");
		CompositeQuery composite = CompositeQuery.create(CompositeModifier.OR, equalQuery, equalQuery2);

		Query query = QueryBuilder.create().add(composite).add(nestedComposite).build();

		List<TestVO> testingData = new ArrayList<TestVO>();

		TestVO bean = new TestVO("filledId");
		bean.setSubObject(new TestVO());
		testingData.add(bean);

		bean = new TestVO("filledId");
		TestVO nestedBean = new TestVO();
		nestedBean.setIntValue(14);
		bean.setSubObject(nestedBean);
		testingData.add(bean); // should pass

		bean = new TestVO();
		nestedBean = new TestVO();
		nestedBean.setIntValue(18);
		bean.setSubObject(nestedBean);
		testingData.add(bean);

		bean = new TestVO("filledId");
		bean.setSubObject(new TestVO("allowed"));
		testingData.add(bean); // should pass

		bean = new TestVO("anotherId");
		bean.setSubObject(new TestVO("allowed"));
		testingData.add(bean);

		bean = new TestVO("force");
		bean.setSubObject(new TestVO("allowed"));
		testingData.add(bean); // should pass

		List<TestVO> filteringResult = InMemoryQueryProcessor.execute(testingData, query);
		Assert.assertNotNull(filteringResult);
		Assert.assertEquals(3, filteringResult.size());
	}

}
