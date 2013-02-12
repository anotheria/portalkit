package net.anotheria.portalkit.services.storage.mongo;

import java.util.UUID;

import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.shared.TestVO;

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
	 * @throws StorageException
	 */
	public static void main(String... args) throws StorageException {
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

		// reading entities by query
		// simple query1
		Query query1 = QueryBuilder.create().add(EqualQuery.create("intValue", 123)).build();
		LOGGER.info("");
		LOGGER.info("---> Find by query[" + query1 + "]: " + service.find(query1));

		// simple query2
		EqualQuery equalQuery2 = EqualQuery.create("intValue", 321);
		SortingQuery sorting2 = SortingQuery.create("booleanValue");
		Query query2 = QueryBuilder.create().add(equalQuery2).setLimit(1).setOffset(0).setSorting(sorting2).build();
		LOGGER.info("");
		LOGGER.info("---> Find by query[" + query2 + "]: " + service.find(query2));

		// simple query3
		EqualQuery equalQuery31 = EqualQuery.create("intValue", 321);
		EqualQuery equalQuery32 = EqualQuery.create("intValue", 654);
		SortingQuery sorting3 = SortingQuery.create("booleanValue");
		CompositeQuery compositeQuery = CompositeQuery.create(CompositeModifier.OR, equalQuery31, equalQuery32);
		Query query3 = QueryBuilder.create().add(compositeQuery).setLimit(1).setOffset(0).setSorting(sorting3).build();
		LOGGER.info("");
		LOGGER.info("---> Find by query[" + query3 + "]: " + service.find(query3));

		// removing entity
		LOGGER.info("");
		LOGGER.info("Deleted: " + service.delete(toCreate.getId()));

		// reading all entities
		LOGGER.info("---> Find all: " + service.findAll());
	}

}
