package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.IdCodeGenerator;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.01.13 18:36
 */
public class JDBCAuthenticationPersistenceServiceImplTest {
	public static final String ENV = "psql";
	//public static final String ENV = "h2";


	private JDBCAuthenticationPersistenceServiceImpl getService() throws Exception{
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", ENV));
		JDBCAuthenticationPersistenceServiceImpl service = new JDBCAuthenticationPersistenceServiceImpl();
		service.cleanupFromUnitTests();
		return service;
	}

	@Test public void testCreateEditDeletePassword() throws Exception{
		JDBCAuthenticationPersistenceServiceImpl service = getService();
		AccountId id = AccountId.generateNew();

		String newPassword = IdCodeGenerator.generateCode(200);
		String password = service.getEncryptedPassword(id);
		assertNull("NO Password should be set yet", password);

		service.saveEncryptedPassword(id, newPassword);
		assertEquals(newPassword, service.getEncryptedPassword(id));

		service.deleteEncryptedPassword(id);
		assertEquals(null, service.getEncryptedPassword(id));
	}

	@Test public void testCreateEditUpdateDeletePassword() throws Exception{
		JDBCAuthenticationPersistenceServiceImpl service = getService();
		AccountId id = AccountId.generateNew();

		String newPassword = IdCodeGenerator.generateCode(200);
		String password = service.getEncryptedPassword(id);
		assertNull("NO Password should be set yet", password);

		service.saveEncryptedPassword(id, newPassword);
		assertEquals(newPassword, service.getEncryptedPassword(id));

		String newnewPassword = IdCodeGenerator.generateCode(200);
		service.saveEncryptedPassword(id, newnewPassword);
		assertEquals(newnewPassword, service.getEncryptedPassword(id));

		service.deleteEncryptedPassword(id);
		assertEquals(null, service.getEncryptedPassword(id));
	}

	@Test public void testDelete() throws Exception{
		JDBCAuthenticationPersistenceServiceImpl service = getService();
		AccountId id1 = AccountId.generateNew();
		AccountId id2 = AccountId.generateNew();

		String newPassword1 = IdCodeGenerator.generateCode(200);
		String newPassword2 = IdCodeGenerator.generateCode(200);

		service.saveEncryptedPassword(id1, newPassword1);
		service.saveEncryptedPassword(id2, newPassword2);

		assertEquals(newPassword1, service.getEncryptedPassword(id1));
		assertEquals(newPassword2, service.getEncryptedPassword(id2));

		service.deleteEncryptedPassword(id1);
		assertEquals(null, service.getEncryptedPassword(id1));
		//ensure the second password is not deleted
		assertEquals(newPassword2, service.getEncryptedPassword(id2));

	}
}
