package net.anotheria.portalkit.services.storage.query;

/**
 * {@link BetweenModifier} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum BetweenModifier {

	/**
	 * Value should be less than max value in query and more than min value in query.
	 */
	EXCLUDING,

	/**
	 * Value should be less than or equal to max value in query and more than or equal to min value in query.
	 */
	INCLUDING;

	/**
	 * Default {@link BetweenModifier}.
	 */
	public static final BetweenModifier DEFAULT = EXCLUDING;

}
