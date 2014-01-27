package net.anotheria.portalkit.services.storage.mongo.util;

/**
 * Mongo constants.
 * 
 * @author Alexandr Bolbat
 */
public final class MongoConstants {

	/**
	 * Field name for document id.
	 */
	public static final String FIELD_ID_NAME = "_id";
	/**
	 * No equal operator.
	 */
	public static final String NOT_EQUAL_OPERATOR = "$ne";

	/**
	 * Private constructor.
	 */
	private MongoConstants() {
		throw new IllegalAccessError();
	}

}
