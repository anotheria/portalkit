package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:22
 */
abstract class JDBCAccountPersistenceServiceImplTest {

	public static final String HSQL = "hsqldb";

	protected JDBCAccountPersistenceServiceImpl getService(String environment) throws Exception{
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
		JDBCAccountPersistenceServiceImpl service = new JDBCAccountPersistenceServiceImpl();
		service.cleanupFromUnitTests();
		return service;
	}

	public Account createAccount(JDBCAccountPersistenceServiceImpl service) throws Exception{
		Account newAcc = createAccountTemplate();
		service.saveAccount(newAcc);
		return newAcc;
	}

	public Account createAccountTemplate(){
		Account account = new Account();
		account.setName("test");
		account.setEmail("test@example.com");
		account.setType(1);
		account.setRegistrationTimestamp(System.currentTimeMillis());
		account.addStatus(1); account.addStatus(16); account.addStatus(32);
		return Account.newAccountFromPattern(account);
	}

	public void testGetAccount(JDBCAccountPersistenceServiceImpl service) throws Exception{
		AccountId any = AccountId.generateNew();
		Account nonExisting = service.getAccount(any);
		assertNull(nonExisting);

		Account created = createAccount(service);
		Account existing = service.getAccount(created.getId());
		assertNotSame(created, existing);
		assertEquals(created, existing);
	}



	public void testDeleteAccount(JDBCAccountPersistenceServiceImpl service) throws Exception{
		AccountId any = AccountId.generateNew();
		service.deleteAccount(any);

		//sofar so good, we can delete nonexisting accounts.


		Account created = createAccount(service);
		Account existing = service.getAccount(created.getId());
		assertEquals(created.getId(), existing.getId());

		service.deleteAccount(existing.getId());

		Account nonexisting = service.getAccount(created.getId());
		assertNull("expected null", nonexisting);
	}




	public void testEditAccount(JDBCAccountPersistenceServiceImpl service) throws Exception{

		Account created = createAccount(service);
		Account existing = service.getAccount(created.getId());
		assertEquals(created.getId(), existing.getId());
		assertEquals(created.getType(), existing.getType());

		existing.setType(100);
		service.saveAccount(existing);
		Account reread = service.getAccount(created.getId());
		assertEquals(created.getId(), reread.getId());
		assertEquals(100, reread.getType());
	}

	public void testAccountFields(JDBCAccountPersistenceServiceImpl service) throws Exception{
		Account toCreate = createAccountTemplate();
		service.saveAccount(toCreate);
		Account fromService = service.getAccount(toCreate.getId());

		assertEquals(toCreate, fromService);
		assertNotSame(toCreate, fromService);

		assertEquals("id mismatch", toCreate.getId(), fromService.getId());
		assertEquals("name mismatch", toCreate.getName(), fromService.getName());
		assertEquals("type mismatch", toCreate.getType(), fromService.getType());
		assertEquals("email mismatch", toCreate.getEmail(), fromService.getEmail());
		assertEquals("status mismatch", toCreate.getStatus(), fromService.getStatus());
		assertEquals("reg timestamp mismatch", toCreate.getRegistrationTimestamp(), fromService.getRegistrationTimestamp());
	}
}
