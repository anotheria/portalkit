package net.anotheria.portalkit.services.storage.query;

/**
 * {@link LessThanQuery} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum LessThanModifier {

	/**
	 * Value should be less than value in query.
	 */
	LESS,

	/**
	 * Value should be less than or equal to value in query.
	 */
	LESS_OR_EQUAL;

	/**
	 * Default {@link LessThanModifier}.
	 */
	public static final LessThanModifier DEFAULT = LESS;

}
