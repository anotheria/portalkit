package net.anotheria.portalkit.engines.mailhunter.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneMatcher extends AbstractMatcher{

	private String genericValue;

	private Matcher matcher;

	private Pattern pattern = Pattern.compile("^[0-9]{9,}$");


	public PhoneMatcher(String aValue, double aReduction){
		super(aReduction);
		genericValue = aValue;
	}

	public PhoneMatcher(String aValue){
		this(aValue, 1.0);
	}

	public PhoneMatcher() {
		this("", 1.0);
	}

	@Override public boolean doesMatch(String expression) {
		matcher = pattern.matcher(expression);
		return matcher.matches();
	}
	
	@Override public String toString(){
		return genericValue;
	}
}
 