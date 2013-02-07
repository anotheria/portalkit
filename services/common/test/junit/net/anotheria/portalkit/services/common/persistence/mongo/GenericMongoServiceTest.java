package net.anotheria.portalkit.services.common.persistence.mongo;

import java.util.UUID;

import net.anotheria.portalkit.services.common.persistence.mongo.exception.StorageException;
import net.anotheria.portalkit.services.common.persistence.mongo.shared.TestVO;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * {@link GenericMongoService} test.
 * 
 * @author Alexandr Bolbat
 */
@Ignore
public class GenericMongoServiceTest {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(GenericMongoServiceTest.class);

	/**
	 * For development purposes.
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws StorageException
	 */
	public static void main(String... args) throws InterruptedException, StorageException {
		GenericMongoServiceImpl<TestVO> service = new GenericMongoServiceImpl<TestVO>(TestVO.class);

		// retrieving meta information
		DB database = service.getMongoClient().getDB(service.getDBName());
		for (String collectionName : database.getCollectionNames()) {
			DBCollection collection = database.getCollection(collectionName);
			LOGGER.info(collectionName + "[" + collection.getCount() + "]");
			DBCursor cursor = collection.find();
			while (cursor.hasNext()) {
				LOGGER.info("\t" + cursor.next());
			}
		}

		// creating new entity
		TestVO toCreate = new TestVO();
		toCreate.setId(UUID.randomUUID().toString());
		toCreate.setIntValue(123);
		toCreate.setBooleanValue(true);
		LOGGER.info("Created: " + service.create(toCreate));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());

		// updating entity
		toCreate.setIntValue(321);
		LOGGER.info("Updated: " + service.update(toCreate));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());

		// saving entity
		TestVO subObject = new TestVO();
		subObject.setId(UUID.randomUUID().toString());
		subObject.setIntValue(456);
		toCreate.setSubObject(subObject);
		LOGGER.info("Saved: " + service.save(toCreate));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());

		// removing entity
		LOGGER.info("Deleted: " + service.delete(toCreate.getId()));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());
	}

}
