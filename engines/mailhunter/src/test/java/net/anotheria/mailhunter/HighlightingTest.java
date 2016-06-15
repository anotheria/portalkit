package net.anotheria.mailhunter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HighlightingTest {
	
	private static TransformationEngine engine = TransformationEngine.createEngine();
	
	@Test public void bla(){
		test("bla bei gmx in Deutschland");
		test("bla bei gustav martin xaver in Deutschland");
		test("ed.xmgättalb");
		test("nachzulesen bei dvayanu affe Guten Morgen Xantippe und so weiter");
	}
	
	private void test(String text){
		assertEquals(1.0, engine.getHighestMatchProbability(text), 0.1);
		MatchDetails details = engine.getMatchDetails();
		System.out.println("Found email in - "+details+" in text "+text);
		//assertTrue(text.indexOf(matchedArea)!=-1);
	}
}
 