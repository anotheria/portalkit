package net.anotheria.portalkit.engines.mailhunter.transformer;

import org.junit.Test;
import static org.junit.Assert.*;

public class CapitalsOnlyTransformerTest {
	CapitalsOnlyTransformer transformer = new CapitalsOnlyTransformer();

	@Test public void testTransformer(){
		assertEquals("AAA", transformer.transform("AaAaAa"));
		assertEquals("TATSCOL", transformer.transform("This is A very long Text with Some \nCapitals and\rOther\tLetters"));
	}
}
