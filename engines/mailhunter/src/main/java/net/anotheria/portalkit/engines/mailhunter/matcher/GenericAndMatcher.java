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
		for (int i=0; i<firstValues.size(); i++){
			String val1 = firstValues.get(i);
			if (expression.indexOf(val1)!=-1){
				for (int y=0; y<secondValues.size(); y++){
					String val2 = secondValues.get(y);
					if (expression.indexOf(val2)!=-1)
						return true;
				}
				return false;
			}
		}
		return false;
	}
	
}
