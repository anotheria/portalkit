package net.anotheria.portalkit.services.storage;

import java.util.List;

import net.anotheria.portalkit.services.storage.shared.TestVO;

import org.junit.Assert;

/**
 * Abstract storage service test.
 * 
 * @author Alexandr Bolbat
 */
public abstract class AbstractStorageServiceTest {

	/**
	 * Validating two entities between each other.
	 * 
	 * @param original
	 * @param toValidate
	 */
	protected static void validateEntity(final TestVO original, final TestVO toValidate) {
		Assert.assertNotNull("Can't be null.", toValidate);
		Assert.assertNotSame("Can't be the same.", original, toValidate);
		Assert.assertEquals("Id should be the same.", original.getId(), toValidate.getId());
		Assert.assertEquals("intValue should be the same.", original.getIntValue(), toValidate.getIntValue());
		Assert.assertEquals("booleanValue should be the same.", original.isBooleanValue(), toValidate.isBooleanValue());
		Assert.assertEquals("subObject should be the same.", original.getSubObject(), toValidate.getSubObject());
	}

	/**
	 * Validate all entities.
	 * 
	 * @param original
	 * @param entities
	 */
	protected static void validateAll(final TestVO original, final List<TestVO> entities) {
		Assert.assertNotNull("Can't be null.", entities);
		Assert.assertEquals("Results amount should be the same.", original != null ? 1 : 0, entities.size());
		if (original != null)
			validateEntity(original, entities.get(0));
	}

}
