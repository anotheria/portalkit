package net.anotheria.portalkit.engines.mailhunter;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTest {
	private TransformationEngine engine = TransformationEngine.createEngine();
	
	@Test public void testSimplePatterns(){
		assertEquals(1.0, engine.getHighestMatchProbability("bla@gmx.de"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@gmx.net"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@web.de"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@yahoo.de"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@hotmail.de"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@hotmail.com"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@yahoo.com"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@gmail.com"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@googlemail.de"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@gmail.de"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla@googlemail.com"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("leon@anotheria.net"), 0.1);
	}

	@Test public void testComplicatedGMXPatterns(){
		assertEquals(1.0, engine.getHighestMatchProbability("bla bei gmx in Deutschland"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("bla bei gustav martin xaver in Deutschland"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("ed.xmgättalb"), 0.1);
		assertEquals(1.0, engine.getHighestMatchProbability("nachzulesen bei dvayanu affe Guten Morgen Xantippe und so weiter"), 0.1);
	}
	
	@After public void tearDown(){
		engine.printOutStats();
	}
	
	@Test public void testPascalsPresentation(){
		//assertEquals(1.0, engine.getHighestMatchProbability("paula without space on the warm post commerce center"), 0.1);
		//assertEquals(1.0, engine.getHighestMatchProbability("paula without space att the warm post dot commerce center"), 0.1);
	}
}
