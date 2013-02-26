package net.anotheria.portalkit.services.storage.mongo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.storage.AbstractStorageServiceTest;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.StorageServiceFactory;
import net.anotheria.portalkit.services.storage.StorageType;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.inmemory.GenericInMemoryService;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * {@link GenericMongoService} test.
 * 
 * @author Alexandr Bolbat
 */
public class GenericMongoServiceTest extends AbstractStorageServiceTest {

	/**
	 * {@link GenericInMemoryService} instance.
	 */
	private StorageService<TestVO> storage;

	/**
	 * Clean-up.
	 * 
	 * @throws MetaFactoryException
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void before() throws MetaFactoryException {
		Map<String, Serializable> factoryParameters = new HashMap<String, Serializable>();
		factoryParameters.put(StorageServiceFactory.PARAMETER_STORAGE_TYPE, StorageType.NOSQL_MONGO_GENERIC);
		factoryParameters.put(GenericMongoServiceFactory.PARAMETER_ENTITY_CLASS, TestVO.class);
		String extension = "MongoPersistence";

		MetaFactory.addParameterizedFactoryClass(StorageService.class, extension, StorageServiceFactory.class, factoryParameters);

		this.storage = MetaFactory.get(StorageService.class, extension);
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
	@Ignore
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

		// removing entity
		TestVO deleted = storage.delete(toCreate.getId());
		validateEntity(updated, deleted);

		// reading all entities
		validateAll(null, storage.findAll());
	}
}
