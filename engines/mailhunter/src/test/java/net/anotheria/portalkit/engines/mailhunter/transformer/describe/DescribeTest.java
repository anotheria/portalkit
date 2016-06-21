package net.anotheria.portalkit.engines.mailhunter.transformer.describe;

import org.junit.Test;
import static org.junit.Assert.*;
public class DescribeTest {
	@Test public void testTransformerInLastPartOfName(){
		assertEquals("Foo", new FooTransformer().describe());
	}
	@Test public void testTransformerInFirstPartOfName(){
		assertEquals("Foo", new TransformerFoo().describe());
	}
	@Test public void testTransformerInMidOfName(){
		assertEquals("Foo", new FooTransformerFoo().describe());
	}
}
