package net.anotheria.mailhunter;

import org.junit.Test;

public class PerformanceTest {
	
	private TransformationEngine engine = TransformationEngine.createEngine();

	private static final long REPEATS = 100000;
	
	@Test public void testHit(){
		long start = System.currentTimeMillis();
		for (int i=0; i<REPEATS; i++){
			engine.getHighestMatchProbability("bla bei gustav martin xaver in Deutschland");
			//engine.getHighestMatchProbability("bla");
		}
		long end = System.currentTimeMillis();
		System.out.println("HIT "+REPEATS+" in "+(end-start)+" ms, "+((double)(end-start)/REPEATS)+" per recogniziton");
	}
	
	@Test public void testNoHit(){
		long start = System.currentTimeMillis();
		for (int i=0; i<REPEATS; i++){
			//engine.getHighestMatchProbability("bla bei gustav martin xaver in Deutschland");
			engine.getHighestMatchProbability("bla");
		}
		long end = System.currentTimeMillis();
		System.out.println("NO HIT "+REPEATS+" in "+(end-start)+" ms, "+((double)(end-start)/REPEATS)+" per recogniziton");
	}
}
