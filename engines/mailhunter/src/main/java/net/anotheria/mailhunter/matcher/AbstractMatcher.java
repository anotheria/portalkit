package net.anotheria.mailhunter.matcher;

import net.anotheria.mailhunter.Matcher;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public abstract class AbstractMatcher implements Matcher{
	
	protected AbstractMatcher(){
		this(1.0);
	}
	
	protected AbstractMatcher(double aProbabilityReduction){
		probabilityReduction = aProbabilityReduction;	
	}
	
	private double probabilityReduction; 
	/**
	 * @return
	 */
	public double getProbabilityReduction() {
		return probabilityReduction;
	}

	/**
	 * @param d
	 */
	public void setProbabilityReduction(double d) {
		probabilityReduction = d;
	}

}
