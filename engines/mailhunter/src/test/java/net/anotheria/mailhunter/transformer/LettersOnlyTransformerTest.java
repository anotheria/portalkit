package net.anotheria.mailhunter.transformer;

import org.junit.Test;
import static org.junit.Assert.*;

public class LettersOnlyTransformerTest {
	LettersOnlyTransformer transformer = new LettersOnlyTransformer();

	@Test public void testTransformer(){
		assertEquals("AAA", transformer.transform("A%A$A§"));
		assertEquals("This is A very long Text with Some Capitals andOtherLetters", transformer.transform("This is A very long Text with Some \nCapitals and\rOther\tLetters"));
		assertEquals("abcdefgh123", transformer.transform("a'$%b&/(c-.,d!\"!ef%'gh+*12{}3"));
	}
}
