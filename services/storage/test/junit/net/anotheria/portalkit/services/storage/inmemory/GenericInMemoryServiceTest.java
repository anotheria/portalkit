package net.anotheria.portalkit.services.storage.inmemory;

import java.util.UUID;

import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.apache.log4j.Logger;
import org.junit.Ignore;

/**
 * {@link GenericInMemoryServiceTest} test.
 * 
 * @author Alexandr Bolbat
 */
@Ignore
public class GenericInMemoryServiceTest {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(GenericInMemoryServiceTest.class);

	/**
	 * For development purposes.
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws StorageException
	 */
	public static void main(String... args) throws InterruptedException, StorageException {
		GenericInMemoryServiceImpl<TestVO> service = new GenericInMemoryServiceImpl<TestVO>("id");

		// creating new entity
		TestVO toCreate = new TestVO();
		toCreate.setId(UUID.randomUUID().toString());
		toCreate.setIntValue(123);
		toCreate.setBooleanValue(true);
		LOGGER.info("");
		LOGGER.info("Created: " + service.create(toCreate));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());

		// updating entity
		toCreate.setIntValue(321);
		LOGGER.info("");
		LOGGER.info("Updated: " + service.update(toCreate));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());

		// saving entity
		TestVO subObject = new TestVO();
		subObject.setId(UUID.randomUUID().toString());
		subObject.setIntValue(456);
		toCreate.setSubObject(subObject);
		LOGGER.info("");
		LOGGER.info("Saved: " + service.save(toCreate));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());

		// removing entity
		LOGGER.info("");
		LOGGER.info("Deleted: " + service.delete(toCreate.getId()));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());
	}

}
