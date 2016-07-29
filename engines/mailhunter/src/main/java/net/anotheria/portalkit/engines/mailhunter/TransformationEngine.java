package net.anotheria.portalkit.engines.mailhunter;

import java.util.ArrayList;

import net.anotheria.portalkit.engines.mailhunter.configurators.PlainConfigurator;
import net.anotheria.util.NumberUtils;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class TransformationEngine {
	
	private ArrayList<Transformation> transformations;
	private ArrayList<Matcher> matchers;
	
	private TransformationStats transStats[]; 
	
	private TransformationEngine(EngineConfigurator configurator){
		matchers = new ArrayList<Matcher>();
		transformations = new ArrayList<Transformation>();
		configurator.configure(this);
		//reorganize?
		//System.out.println("Ready configured, transformations: "+transformations+", matcher: "+matchers);
		transStats = new TransformationStats[transformations.size()];
		for (int i=0; i<transStats.length; i++)
			transStats[i] = new TransformationStats(transformations.get(i).describe());
	}

	public double getHighestMatchProbability(String toCheck) {
		return getHighestMatchProbability(toCheck, "");
	}

	public double getHighestMatchProbability(String toCheck, String locale){
		double result = 0.0;
		long startTime;
		TransformationContext.getContext().setToMatch(toCheck);
		for (int i=0; i<transformations.size(); i++){
			Transformation t = transformations.get(i);
			startTime = System.currentTimeMillis();
			transStats[i].notifyRequest();
			String transformedToCheck = t.transform(toCheck, locale);
			for (int y=0; y<matchers.size(); y++){
				Matcher m = matchers.get(y);
				boolean match = m.doesMatch(transformedToCheck);
				if (match)
					result = 1.0;
				if (result==1.0){
					//System.out.println(toCheck+" matched on "+m);
					transStats[i].notifyHit();
					transStats[i].notifyDuration(System.currentTimeMillis()-startTime);
					
					TransformationContext.getContext().setFiredTransformation(t);
					TransformationContext.getContext().setFiredMatcher(m);
					
					return result; 
				}
			}
			transStats[i].notifyDuration(System.currentTimeMillis()-startTime);
		}
		return result;
	}
	
	public MatchDetails getMatchDetails(){
		TransformationContext context = TransformationContext.getContext();
		if (context.getFiredMatcher()==null || context.getFiredTransformation()==null)
			throw new IllegalStateException("getMatchedArea() can only be called after a positive match by getHighestMatchProbability");
		if (context.getToMatch()==null)
			throw new IllegalStateException("No saved match text");
		
		MatchDetails details = new MatchDetails();
		
		//find beginning of the word
		String toMatch = context.getToMatch(); 
		String toCheck = null; 
		Transformation t = context.getFiredTransformation();
		details.setMatchedPattern(t.transform(toMatch));
		Matcher m = context.getFiredMatcher();
		int start = 0;
		boolean foundStart = false;
		
		toMatch = context.getToMatch();
		while(!foundStart){
			toCheck = t.transform(toMatch);
			
			foundStart = !m.doesMatch(toCheck);
			if (!foundStart){
				start++; toMatch = toMatch.substring(1);
			}
		}
		
		if (start>0)
			start--;

		
		toMatch = context.getToMatch();
		int end = toMatch.length();
		boolean foundEnd = false;
		while(!foundEnd){
			toCheck = t.transform(toMatch);
			foundEnd = !m.doesMatch(toCheck);
			if (!foundEnd){
				end--; toMatch = toMatch.substring(0, end);
			}
		}
		
		if (end<toMatch.length()-1)
			end++;
		
		details.setStart(start);
		details.setEnd(end);

		if (start < end) {
			details.setMatchedArea(context.getToMatch().substring(start, end + 1));
		} else {
			details.setMatchedArea(context.getToMatch());
		}

		return details;
	}

	public boolean doesMatch(String toCheck, double probabilityTreshhold) {
		return doesMatch(toCheck, probabilityTreshhold, "");
	}
	
	public boolean doesMatch(String toCheck, double probabilityTreshhold, String locale){
		double result = getHighestMatchProbability(toCheck, locale);
		return result>=probabilityTreshhold;
	}
	
	public void addMatcher(Matcher m){
		matchers.add(m);
	}
	
	public void addTransformation(Transformation t){
		transformations.add(t);
	}
	
	public void printOutStats(){
		System.out.println("==== Transformation stats: ====");
		for (int i=0; i<transStats.length; i++)
			System.out.println(NumberUtils.itoa(i+1,3)+" "+transStats[i]);
	}
	
	public static TransformationEngine createEngine(EngineConfigurator configurator){
		return new TransformationEngine(configurator);
	}
	
	public static TransformationEngine createEngine(){
		return createEngine(new PlainConfigurator());
	}
}
