package net.anotheria.portalkit.services.storage.query;

/**
 * {@link MoreThanModifier} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum MoreThanModifier {

	/**
	 * Value should be more than value in query.
	 */
	MORE,

	/**
	 * Value should be more than or equal to value in query.
	 */
	MORE_OR_EQUAL;

	/**
	 * Default {@link MoreThanModifier}.
	 */
	public static final MoreThanModifier DEFAULT = MORE;

}
