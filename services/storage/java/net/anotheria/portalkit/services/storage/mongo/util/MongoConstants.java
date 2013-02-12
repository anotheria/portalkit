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
	 * Operator name for sorting.
	 */
	public static final String OPERATOR_SORTING = "$sort";

	/**
	 * Private constructor.
	 */
	private MongoConstants() {
		throw new IllegalAccessError();
	}

}
