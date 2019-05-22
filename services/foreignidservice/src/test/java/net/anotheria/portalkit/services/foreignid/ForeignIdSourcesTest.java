package net.anotheria.portalkit.services.foreignid;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 2019-05-22 09:31
 */
public class ForeignIdSourcesTest {
	@Test public void testOffsetIsAddedIfCustomIdBelow1000(){
		assertEquals(1015, ForeignIdSources.getCustomIdWithOffset(15));
	}

	@Test public void testOffsetIsNotAddedIfCustomIdAbove1000(){
		assertEquals(1020, ForeignIdSources.getCustomIdWithOffset(1020));
	}

}
