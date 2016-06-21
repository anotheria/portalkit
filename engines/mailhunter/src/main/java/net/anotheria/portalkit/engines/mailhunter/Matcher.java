package net.anotheria.portalkit.engines.mailhunter;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public interface Matcher {
	boolean doesMatch(String expression);
	
	double getProbabilityReduction();
}
