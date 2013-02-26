package net.anotheria.portalkit.services.storage.inmemory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.storage.AbstractStorageServiceTest;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.StorageServiceFactory;
import net.anotheria.portalkit.services.storage.StorageType;
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
	 * 
	 * @throws MetaFactoryException
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void before() throws MetaFactoryException {
		Map<String, Serializable> factoryParameters = new HashMap<String, Serializable>();
		factoryParameters.put(StorageServiceFactory.PARAMETER_STORAGE_TYPE, StorageType.IN_MEMORY_GENERIC);
		factoryParameters.put(GenericInMemoryServiceFactory.PARAMETER_ENTITY_KEY_FIELD_NAME, "id");
		String extension = "InMemoryService";

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
