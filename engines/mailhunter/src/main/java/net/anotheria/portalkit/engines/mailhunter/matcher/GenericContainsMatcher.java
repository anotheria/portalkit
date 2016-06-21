package net.anotheria.portalkit.engines.mailhunter.matcher;

public class GenericContainsMatcher extends AbstractMatcher{
	
	private String genericValue;
	

	public GenericContainsMatcher(String aValue, double aReduction){
		super(aReduction);
		genericValue = aValue;
	}

	public GenericContainsMatcher(String aValue){
		this(aValue, 1.0);
	}

	@Override public boolean doesMatch(String expression) {
		return expression.indexOf(genericValue)!=-1;
	}
	
	@Override public String toString(){
		return genericValue;
	}
}
 