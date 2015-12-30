package net.anotheria.portalkit.services.storage.query;

import com.mongodb.BasicDBObject;

/**
 * Custom query interface.
 *
 * @author Alexandr Bolbat
 */
public interface CustomQuery extends Query {
	/**
	 * Maps custom query implementation to mongo query format.
	 *
	 * @return {@link BasicDBObject}
	 */
	BasicDBObject map();
}
