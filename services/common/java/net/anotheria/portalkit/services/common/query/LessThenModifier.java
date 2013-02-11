package net.anotheria.portalkit.services.common.query;

/**
 * {@link LessThenQuery} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum LessThenModifier {

	/**
	 * Value should be less then value in query.
	 */
	LESS,

	/**
	 * Value should be less then or equal to value in query.
	 */
	LESS_OR_EQUAL;

	/**
	 * Default {@link LessThenModifier}.
	 */
	public static final LessThenModifier DEFAULT = LESS;

}
