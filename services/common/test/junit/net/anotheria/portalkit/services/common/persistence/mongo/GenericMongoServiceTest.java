package net.anotheria.portalkit.services.common.persistence.mongo;

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
	 */
	public static void main(String... args) throws InterruptedException {
		GenericMongoServiceImpl service = new GenericMongoServiceImpl();

		DB database = service.getMongoClient().getDB(service.getDBName());
		for (String collectionName : database.getCollectionNames()) {
			DBCollection collection = database.getCollection(collectionName);
			LOGGER.info(collectionName + "[" + collection.getCount() + "]");
			DBCursor cursor = collection.find();
			while (cursor.hasNext()) {
				LOGGER.info("\t" + cursor.next());
			}
		}
	}

}
