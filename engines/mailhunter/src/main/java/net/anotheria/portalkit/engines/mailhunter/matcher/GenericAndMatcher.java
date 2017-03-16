package net.anotheria.portalkit.engines.mailhunter.matcher;

import java.util.ArrayList;
import java.util.List;

public class GenericAndMatcher extends AbstractMatcher{
	
	private ArrayList<String> firstValues;
	private ArrayList<String> secondValues;

	public GenericAndMatcher(List<String> aFirstValues, List<String> aSecondValues, double aReduction){
		super(aReduction);
		
		firstValues = new ArrayList<String>();
		if (aFirstValues!=null)
			firstValues.addAll(aFirstValues);
		
		secondValues = new ArrayList<String>();
		if (aSecondValues!=null)
			secondValues.addAll(aSecondValues);
		
	}
	
	public void addFirstValue(String aValue){
		firstValues.add(aValue);
	}
	
	public void addSecondValue(String aValue){
		secondValues.add(aValue);
	}

	public boolean doesMatch(String expression) {
		for (String val1 : firstValues) {
			int index1 = expression.indexOf(val1);
			if (index1 != -1) {
				for (String val2 : secondValues) {
					int index2 = expression.indexOf(val2);
					if (index2 > index1)
						return true;
				}
			}
		}
		return false;
	}
	
}
