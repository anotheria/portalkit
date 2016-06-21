package net.anotheria.portalkit.engines.mailhunter.transformer;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class StartingLettersTransformerTest {
	StartingLettersTransformer transformer = new StartingLettersTransformer();

	//TODO impl this test
	@Ignore @Test public void testTransformer(){
		assertEquals("AAA", transformer.transform("A% A$ A§"));
		assertEquals("This is A very long Text with Some Capitals andOtherLetters", transformer.transform("This is A very long Text with Some \nCapitals and\rOther\tLetters"));
		assertEquals("abcdefgh123", transformer.transform("a$%b&/(c-.,d!\"ef'gh+*12{}3"));
	}
}
