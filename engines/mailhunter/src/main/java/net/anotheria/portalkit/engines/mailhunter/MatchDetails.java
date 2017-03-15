package net.anotheria.portalkit.engines.mailhunter;

public class MatchDetails {
	private String transformedText;
	private String matchedPattern;
	private String matchedArea;
	private int start;
	private int end;

	public String getTransformedText() {
		return transformedText;
	}

	public void setTransformedText(String transformedText) {
		this.transformedText = transformedText;
	}

	public String getMatchedPattern() {
		return matchedPattern;
	}

	public void setMatchedPattern(String matchedPattern) {
		this.matchedPattern = matchedPattern;
	}

	public String getMatchedArea() {
		return matchedArea;
	}

	public void setMatchedArea(String matchedArea) {
		this.matchedArea = matchedArea;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	
	@Override public String toString(){
		return matchedPattern+" in "+matchedArea+" ["+start+":"+end+"]";
	}
}
 