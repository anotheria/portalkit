package net.anotheria.portalkit.services.storage.inmemory;

import java.util.List;
import java.util.UUID;

import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.shared.InheritanceVO;
import net.anotheria.portalkit.services.storage.shared.TestVO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link GenericInMemoryServiceTest} test.
 * 
 * @author Alexandr Bolbat
 */
public class GenericInMemoryServiceTest {

	/**
	 * {@link StorageService} instance.
	 */
	private StorageService<TestVO> storage;

	/**
	 * {@link StorageService} instance.
	 */
	private StorageService<InheritanceVO> inheritanceStorage;

	/**
	 * Clean-up.
	 */
	@Before
	public void before() {
		this.storage = new GenericInMemoryServiceImpl<TestVO>("id");
		this.inheritanceStorage = new GenericInMemoryServiceImpl<InheritanceVO>("id");
	}

	/**
	 * Initialization.
	 */
	@After
	public void after() {
		this.storage = null;
		this.inheritanceStorage = null;
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

		//validate null value
		List<TestVO> nullResult = storage.find(EqualQuery.create("intValues", EqualQuery.NullModifier.NULL));
		Assert.assertNotNull("Result should not be be null", nullResult);
		Assert.assertEquals("Result should have values inside", 1, nullResult.size());

		//validate not null value
		List<TestVO> notNullResult = storage.find(EqualQuery.create("intValue", EqualQuery.NullModifier.NOT_NULL));
		Assert.assertNotNull("Result should not be be null", notNullResult);
		Assert.assertEquals("Result should have values inside", 1, notNullResult.size());


		// reading all entities
		validateAll(updated, storage.findAll());

		// removing entity
		TestVO deleted = storage.delete(toCreate.getId());
		validateEntity(updated, deleted);

		// reading all entities
		validateAll(null, storage.findAll());
	}

	/**
	 * Entity inheritance test.
	 * 
	 * @throws StorageException
	 */
	@Test
	public void inheritanceTest() throws StorageException {
		// preparing new entity
		InheritanceVO toCreate = new InheritanceVO();
		toCreate.setId(UUID.randomUUID().toString());
		// creating new entity
		InheritanceVO created = inheritanceStorage.create(toCreate);
		validateEntity(toCreate, created);

		// reading all entities
		validateAll(created, inheritanceStorage.findAll());

		// updating entity
		InheritanceVO toUpdate = created;
		toUpdate.setIntValue(321);
		InheritanceVO updated = inheritanceStorage.update(toUpdate);
		validateEntity(created, updated);

		// reading all entities
		validateAll(updated, inheritanceStorage.findAll());

		// saving entity
		InheritanceVO subObject = new InheritanceVO();
		subObject.setId(UUID.randomUUID().toString());
		subObject.setIntValue(456);
		toUpdate = updated;
		toUpdate.setSubObject(subObject);
		updated = inheritanceStorage.save(toUpdate);
		validateEntity(toUpdate, updated);

		// reading all entities
		validateAll(updated, inheritanceStorage.findAll());

		// removing entity
		InheritanceVO deleted = inheritanceStorage.delete(toCreate.getId());
		validateEntity(updated, deleted);

		// reading all entities
		validateAll(null, inheritanceStorage.findAll());

	}

	/**
	 * Validating two entities between each other.
	 * 
	 * @param original
	 * @param toValidate
	 */
	private static void validateEntity(final TestVO original, final TestVO toValidate) {
		Assert.assertNotNull("Can't be null.", toValidate);
		Assert.assertNotSame("Can't be the same.", original, toValidate);
		Assert.assertEquals("Id should be the same.", original.getId(), toValidate.getId());
		Assert.assertEquals("intValue should be the same.", original.getIntValue(), toValidate.getIntValue());
		Assert.assertEquals("booleanValue should be the same.", original.isBooleanValue(), toValidate.isBooleanValue());
		Assert.assertEquals("subObject should be the same.", original.getSubObject(), toValidate.getSubObject());
	}

	/**
	 * Validate all entities.
	 * 
	 * @param original
	 * @param entities
	 */
	private static void validateAll(final TestVO original, final List<? extends TestVO> entities) {
		Assert.assertNotNull("Can't be null.", entities);
		Assert.assertEquals("Results amount should be the same.", original != null ? 1 : 0, entities.size());
		if (original != null)
			validateEntity(original, entities.get(0));
	}

}
