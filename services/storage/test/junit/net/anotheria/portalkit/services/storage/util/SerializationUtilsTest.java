package net.anotheria.portalkit.services.storage.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link SerializationUtils} test.
 * 
 * @author Alexandr Bolbat
 */
public class SerializationUtilsTest {

	/**
	 * Testing object cloning.
	 */
	@Test
	public void testObjectClone() {
		SerializableBean toClone = new SerializableBean("testing");
		SerializableBean cloned = SerializationUtils.clone(toClone);
		Assert.assertNotNull(cloned);
		Assert.assertNotSame(toClone, cloned);
		Assert.assertNotNull(cloned.getValue());
		Assert.assertEquals("testing", cloned.getValue());
		Assert.assertNotSame(toClone.getValue(), cloned.getValue());
		Assert.assertEquals(toClone.getValue(), cloned.getValue());
	}

	/**
	 * Testing {@link Collection} cloning.
	 */
	@Test
	public void testCollectionClone() {
		List<SerializableBean> toClone = new ArrayList<SerializableBean>();
		for (int i = 0; i < 10; i++)
			toClone.add(new SerializableBean("testing_" + i));

		List<SerializableBean> cloned = SerializationUtils.clone(toClone);
		Assert.assertNotNull(cloned);
		Assert.assertNotSame(toClone, cloned);
		Assert.assertEquals(toClone.size(), cloned.size());
		Assert.assertEquals(10, cloned.size());
		for (int i = 0; i < 10; i++) {
			Assert.assertNotNull(cloned.get(i));
			Assert.assertNotSame(toClone.get(i), cloned.get(i));
			Assert.assertNotNull(cloned.get(i).getValue());
			Assert.assertEquals("testing_" + i, cloned.get(i).getValue());
			Assert.assertNotSame(toClone.get(i).getValue(), cloned.get(i).getValue());
			Assert.assertEquals(toClone.get(i).getValue(), cloned.get(i).getValue());
		}
	}

	/**
	 * Test error cases.
	 */
	@Test
	public void testErrorCases() {
		try {
			Serializable nullBean = null;
			SerializationUtils.clone(nullBean);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("toClone"));
		}
		try {
			Collection<Serializable> nullCollection = null;
			SerializationUtils.clone(nullCollection);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("toClone"));
		}
	}

}
