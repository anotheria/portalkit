package net.anotheria.mailhunter.transformer;

import org.junit.Test;
import static org.junit.Assert.*;

public class NoOpTransformerTest {
	NoOpTransformer transformer = new NoOpTransformer();

	static final String TESTSTRINGS[] = {
		"A%A$A%",
		"This is A very long Text with Some \nCapitals and\rOther\tLetters",
		"aö$%b&/(c-.,d!\"ef'gh+*12{}3",
	};
	
	@Test public void testTransformer(){
		for (String s : TESTSTRINGS)
			assertEquals(s, transformer.transform(s));
	}
}
