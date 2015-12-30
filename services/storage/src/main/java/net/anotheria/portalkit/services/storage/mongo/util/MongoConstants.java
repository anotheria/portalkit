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
	public static final String OPERATOR_NOT_EQUAL = "$ne";
	/**
	 * In operator.
	 */
	public static final String OPERATOR_IN = "$in";
	/**
	 * No in operator.
	 */
	public static final String OPERATOR_NOT_IN = "$nin";
	/**
	 * Less then operator.
	 */
	public static final String OPERATOR_LESS_THAN = "$lt";
	/**
	 * Less then or equal operator.
	 */
	public static final String OPERATOR_LESS_THAN_OR_EQUAL = "$lte";
	/**
	 * Greater then operator.
	 */
	public static final String OPERATOR_GREATER_THAN = "$gt";
	/**
	 * Greater then or equal operator.
	 */
	public static final String OPERATOR_GREATER_THAN_OR_EQUAL = "$gte";

	/**
	 * Private constructor.
	 */
	private MongoConstants() {
		throw new IllegalAccessError();
	}

}
