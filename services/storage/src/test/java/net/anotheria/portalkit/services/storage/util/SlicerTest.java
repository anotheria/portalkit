package net.anotheria.portalkit.services.storage.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link Slicer} utility test.
 * 
 * @author Alexandr Bolbat
 */
public class SlicerTest {

	/**
	 * Testing slicing.
	 */
	@Test
	public void testSlicing() {
		List<String> originalList = new ArrayList<String>();

		// checking default values on wrong arguments
		Assert.assertEquals(0, Slicer.slice(null, 1, 10).size());
		Assert.assertEquals(0, Slicer.slice(originalList, -1, -10).size());

		// checking on empty original list
		Assert.assertEquals(0, Slicer.slice(originalList, 1, 10).size());

		// checking with different parameters
		originalList.add("test1");
		Assert.assertEquals(0, Slicer.slice(originalList, 1, 0).size());
		Assert.assertEquals(0, Slicer.slice(originalList, 0, 0).size());
		Assert.assertEquals(0, Slicer.slice(originalList, 2, 10).size());

		Assert.assertEquals(1, Slicer.slice(originalList, 0, 10).size());
		Assert.assertEquals("test1", Slicer.slice(originalList, 0, 10).get(0));

		Assert.assertEquals(1, Slicer.slice(originalList, 0, 10).size());
		Assert.assertEquals("test1", Slicer.slice(originalList, 0, 10).get(0));

		originalList.add("test2");
		originalList.add("test3");
		Assert.assertEquals(0, Slicer.slice(originalList, 1, 0).size());
		Assert.assertEquals(0, Slicer.slice(originalList, 0, 0).size());

		Assert.assertEquals(2, Slicer.slice(originalList, 1, 10).size());
		Assert.assertEquals("test2", Slicer.slice(originalList, 1, 10).get(0));
		Assert.assertEquals("test3", Slicer.slice(originalList, 1, 10).get(1));

		Assert.assertEquals(3, Slicer.slice(originalList, 0, 10).size());
		Assert.assertEquals("test1", Slicer.slice(originalList, 0, 10).get(0));
		Assert.assertEquals("test2", Slicer.slice(originalList, 0, 10).get(1));
		Assert.assertEquals("test3", Slicer.slice(originalList, 0, 10).get(2));

		Assert.assertEquals(3, Slicer.slice(originalList, 0, 10).size());
		Assert.assertEquals("test1", Slicer.slice(originalList, 0, 10).get(0));
		Assert.assertEquals("test2", Slicer.slice(originalList, 0, 10).get(1));
		Assert.assertEquals("test3", Slicer.slice(originalList, 0, 10).get(2));

		Assert.assertEquals(1, Slicer.slice(originalList, 2, 1).size());
		Assert.assertEquals("test2", Slicer.slice(originalList, 1, 1).get(0));
	}

	/**
	 * Testing paging.
	 */
	@Test
	public void testPaging() {
		List<String> originalList = new ArrayList<String>();

		// checking default values on wrong arguments
		Assert.assertEquals(0, Slicer.sliceTo(null, 1, 10).size());
		Assert.assertEquals(0, Slicer.sliceTo(originalList, 1, 1).size());

		// checking on empty original list
		Assert.assertEquals(0, Slicer.sliceTo(originalList, 1, 10).size());

		// checking with different parameters
		originalList.add("test1");
		Assert.assertEquals(1, Slicer.sliceTo(originalList, 0, 1).size());
		Assert.assertEquals("test1", Slicer.sliceTo(originalList, 0, 1).get(0));
		Assert.assertEquals(0, Slicer.sliceTo(originalList, 1, 1).size());
		Assert.assertEquals(0, Slicer.sliceTo(originalList, 1, 10).size());

		Assert.assertEquals(1, Slicer.sliceTo(originalList, 0, 10).size());
		Assert.assertEquals("test1", Slicer.sliceTo(originalList, 0, 10).get(0));

		originalList.add("test2");
		originalList.add("test3");
		Assert.assertEquals(0, Slicer.sliceTo(originalList, 1, 0).size());
		Assert.assertEquals(0, Slicer.sliceTo(originalList, 0, 0).size());

		Assert.assertEquals(0, Slicer.sliceTo(originalList, 2, 10).size());

		Assert.assertEquals(1, Slicer.sliceTo(originalList, 2, 1).size());
		Assert.assertEquals("test3", Slicer.sliceTo(originalList, 2, 1).get(0));

		Assert.assertEquals(3, Slicer.sliceTo(originalList, 0, 10).size());
		Assert.assertEquals("test1", Slicer.sliceTo(originalList, 0, 10).get(0));
		Assert.assertEquals("test2", Slicer.sliceTo(originalList, 0, 10).get(1));
		Assert.assertEquals("test3", Slicer.sliceTo(originalList, 0, 10).get(2));

		Assert.assertEquals(1, Slicer.sliceTo(originalList, 1, 2).size());
		Assert.assertEquals("test3", Slicer.sliceTo(originalList, 1, 2).get(0));
	}

}
