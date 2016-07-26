package net.anotheria.portalkit.engines.mailhunter;

public interface Transformer {
	/**
	 * Transforms a string into another string.
	 *
	 * @param s		string.
	 *
	 * @return transformed string.
	 */
	String transform(String s);

	/**
	 * Transforms a string into another string depending on locale.
	 *
	 * @param s		string.
	 * @param locale locale.
	 *
	 * @return transformed string.
	 */
	String transform(String s, String locale);

	int getOrder();
	
	/**
	 * Returns a string that describe what this transformer does.
	 * @return
	 */
	String describe();
}

