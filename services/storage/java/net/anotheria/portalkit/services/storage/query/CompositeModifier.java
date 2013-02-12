package net.anotheria.portalkit.services.storage.query;

/**
 * {@link CompositeQuery} modifier.
 * 
 * @author Alexandr Bolbat
 */
public enum CompositeModifier {

	/**
	 * All queries in {@link CompositeQuery} should pass.
	 */
	AND,

	/**
	 * One or more queries in {@link CompositeQuery} should pass.
	 */
	OR;

	/**
	 * Default {@link CompositeModifier}.
	 */
	public static final CompositeModifier DEFAULT = AND;

}
