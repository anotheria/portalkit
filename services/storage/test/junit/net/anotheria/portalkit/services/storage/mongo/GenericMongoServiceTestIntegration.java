package net.anotheria.portalkit.services.storage.mongo;

import java.util.List;
import java.util.UUID;

import net.anotheria.portalkit.services.storage.AbstractStorageServiceTest;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.inmemory.GenericInMemoryService;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.ContainsQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link GenericMongoService} test.
 * 
 * @author Alexandr Bolbat
 */
public class GenericMongoServiceTestIntegration extends AbstractStorageServiceTest {

	/**
	 * {@link GenericInMemoryService} instance.
	 */
	private StorageService<TestVO> storage;

	/**
	 * Clean-up.
	 */
	@Before
	public void before() {
		this.storage = new GenericMongoServiceImpl<TestVO>(TestVO.class);
	}

	/**
	 * Initialization.
	 */
	@After
	public void after() {
		this.storage = null;
	}

	/**
	 * Complex test.
	 * 
	 * @throws StorageException
	 */
	@Test
	public void complexTest() throws StorageException {
		// preparing new entity
		TestVO toCreate = new TestVO(UUID.randomUUID().toString());
		toCreate.setIntValue(123);
		toCreate.setBooleanValue(true);

		// creating new entity
		TestVO created = storage.create(toCreate);
		validateEntity(toCreate, created);

		// reading all entities
		validateAll(created, storage.findAll());

		// updating entity
		TestVO toUpdate = created;
		toUpdate.setIntValue(321);
		TestVO updated = storage.update(toUpdate);
		validateEntity(created, updated);

		// reading all entities
		validateAll(updated, storage.findAll());

		// saving entity
		TestVO subObject = new TestVO(UUID.randomUUID().toString());
		subObject.setIntValue(456);
		toUpdate = updated;
		toUpdate.setSubObject(subObject);
		updated = storage.save(toUpdate);
		validateEntity(toUpdate, updated);

		// reading all entities
		validateAll(updated, storage.findAll());

		// reading entities by query
		// simple query1
		Query query1 = QueryBuilder.create().add(EqualQuery.create("intValue", 123)).build();
		List<TestVO> queryResult1 = storage.find(query1);
		validateAll(null, queryResult1);

		// simple query2
		EqualQuery equalQuery2 = EqualQuery.create("intValue", 321);
		SortingQuery sorting2 = SortingQuery.create("booleanValue");
		Query query2 = QueryBuilder.create().add(equalQuery2).setLimit(1).setOffset(0).setSorting(sorting2).build();
		List<TestVO> queryResult2 = storage.find(query2);
		validateAll(updated, queryResult2);

		// simple query3
		EqualQuery equalQuery31 = EqualQuery.create("intValue", 321);
		EqualQuery equalQuery32 = EqualQuery.create("intValue", 654);
		SortingQuery sorting3 = SortingQuery.create("booleanValue");
		CompositeQuery compositeQuery = CompositeQuery.create(CompositeModifier.OR, equalQuery31, equalQuery32);
		Query query3 = QueryBuilder.create().add(compositeQuery).setLimit(1).setOffset(0).setSorting(sorting3).build();
		List<TestVO> queryResult3 = storage.find(query3);
		validateAll(updated, queryResult3);

		// contains query1
		ContainsQuery containsQuery = ContainsQuery.create("intValue", 123);
		List<TestVO> containsResult = storage.find(containsQuery);
		Assert.assertNotNull(containsResult);
		Assert.assertEquals(0, containsResult.size());
		// contains query2
		containsQuery = ContainsQuery.create("intValue", 321);
		containsResult = storage.find(containsQuery);
		Assert.assertNotNull(containsResult);
		Assert.assertEquals(1, containsResult.size());
		validateEntity(updated, containsResult.get(0));
		// contains query3
		containsQuery = ContainsQuery.create("intValue", 321, 123);
		containsResult = storage.find(containsQuery);
		Assert.assertNotNull(containsResult);
		Assert.assertEquals(1, containsResult.size());
		validateEntity(updated, containsResult.get(0));

		// removing entity
		TestVO deleted = storage.delete(toCreate.getId());
		validateEntity(updated, deleted);

		// reading all entities
		validateAll(null, storage.findAll());
	}

}
