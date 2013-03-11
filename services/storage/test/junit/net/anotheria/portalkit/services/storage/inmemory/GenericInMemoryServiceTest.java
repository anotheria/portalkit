package net.anotheria.portalkit.services.storage.inmemory;

import java.util.UUID;

import net.anotheria.portalkit.services.storage.AbstractStorageServiceTest;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link GenericInMemoryServiceTest} test.
 * 
 * @author Alexandr Bolbat
 */
public class GenericInMemoryServiceTest extends AbstractStorageServiceTest {

	/**
	 * {@link GenericInMemoryService} instance.
	 */
	private StorageService<TestVO> storage;

	/**
	 * Clean-up.
	 */
	@Before
	public void before() {
		this.storage = new GenericInMemoryServiceImpl<TestVO>("id");
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

		// removing entity
		TestVO deleted = storage.delete(toCreate.getId());
		validateEntity(updated, deleted);

		// reading all entities
		validateAll(null, storage.findAll());
	}

}
