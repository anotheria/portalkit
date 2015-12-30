package net.anotheria.portalkit.services.storage.util;

import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.shared.InheritanceVO;
import net.anotheria.portalkit.services.storage.shared.TestVO;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link EntityUtils} test.
 *
 * @author ivanbatura
 */
public class EntityUtilsTest {
	@Test
	public void isFieldExistTest() {
		//call tests method
		boolean resultInheritance = EntityUtils.isFieldExist(InheritanceVO.class, "intValue");
		boolean resultOrigin = EntityUtils.isFieldExist(TestVO.class, "intValue");
		//verification
		Assert.assertTrue("Result of the isFieldExist invalid for inheritance", resultInheritance);
		Assert.assertTrue("Result of the isFieldExist invalid for origin", resultOrigin);
	}

	@Test
	public void getFieldValueTest() throws StorageException {
		//preparation
		int inValue = 500;
		InheritanceVO inheritanceVO = new InheritanceVO();
		inheritanceVO.setIntValue(inValue);

		TestVO testVO = new TestVO();
		testVO.setIntValue(inValue);
		//call tests method
		String resultInheritance = EntityUtils.getFieldValue(inheritanceVO, "intValue");
		String resultOrigin = EntityUtils.getFieldValue(testVO, "intValue");
		//verification
		Assert.assertEquals("Result of the getFieldValue invalid for inheritance", String.valueOf(inValue), resultInheritance);
		Assert.assertEquals("Result of the getFieldValue invalid for origin", String.valueOf(inValue), resultOrigin);
	}
}
