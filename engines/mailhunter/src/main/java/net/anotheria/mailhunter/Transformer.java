package net.anotheria.mailhunter;

public interface Transformer {
	/**
	 * Transforms a string into another string.
	 * @param s
	 * @return
	 */
	String transform(String s);
	
	int getOrder();
	
	/**
	 * Returns a string that describe what this transformer does.
	 * @return
	 */
	String describe();
}

