package net.anotheria.portalkit.engines.mailhunter.transformer;

import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveDuplicateLettersTransformerTest {
	RemoveDuplicateLettersTransformer transformer = new RemoveDuplicateLettersTransformer();

	@Test public void testTransformer(){
		assertEquals("A", transformer.transform("AAAAAA"));
		assertEquals("mail", transformer.transform("mmaaaiiiilllll"));

		assertEquals("A", transformer.transform("A"));
		assertEquals("", transformer.transform(""));

	}
}
