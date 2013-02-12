package net.anotheria.portalkit.services.storage.query;

/**
 * {@link BetweenModifier} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum BetweenModifier {

	/**
	 * Value should be less then max value in query and more then min value in query.
	 */
	EXCLUDING,

	/**
	 * Value should be less then or equal to max value in query and more then or equal to min value in query.
	 */
	INCLUDING;

	/**
	 * Default {@link BetweenModifier}.
	 */
	public static final BetweenModifier DEFAULT = EXCLUDING;

}
