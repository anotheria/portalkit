package net.anotheria.mailhunter.matcher;


public class GenericExactMatcher extends AbstractMatcher{
	
	private String genericValue;
	
	public GenericExactMatcher(String aValue, double aReduction){
		super(aReduction);
		genericValue = aValue;
	}
	
	public GenericExactMatcher(String aValue){
		this(aValue, 1.0);
	}
	
	public boolean doesMatch(String expression) {
		return genericValue.equalsIgnoreCase(expression);
	}

	public String toString(){
		return genericValue;
	}
 

}
