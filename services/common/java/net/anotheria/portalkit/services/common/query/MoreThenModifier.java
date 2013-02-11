package net.anotheria.portalkit.services.common.query;

/**
 * {@link MoreThenModifier} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum MoreThenModifier {

	/**
	 * Value should be more then value in query.
	 */
	MORE,

	/**
	 * Value should be more then or equal to value in query.
	 */
	MORE_OR_EQUAL;

	/**
	 * Default {@link MoreThenModifier}.
	 */
	public static final MoreThenModifier DEFAULT = MORE;

}
