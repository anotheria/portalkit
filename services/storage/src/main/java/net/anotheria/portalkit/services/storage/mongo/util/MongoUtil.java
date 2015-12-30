package net.anotheria.portalkit.services.storage.mongo.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo utility.
 * 
 * @author Alexandr Bolbat
 */
public final class MongoUtil {

	/**
	 * Private constructor.
	 */
	private MongoUtil() {
		throw new IllegalAccessError();
	}

	/**
	 * Create {@link DBObject} query for getting object by its unique identifier.
	 * 
	 * @param uid
	 *            unique identifier
	 * @return {@link DBObject} query
	 */
	public static DBObject queryGetEntity(final String uid) {
		return new BasicDBObject(MongoConstants.FIELD_ID_NAME, String.valueOf(uid));
	}

}
