package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:22
 */
public class JDBCAccountPersistenceServiceImplTest {

	public static final String HSQL = "hsqldb";
	public static final String H2 = "h2";
	public static final String PSQL = "psql";

	private JDBCAccountPersistenceServiceImpl getService(String environment) throws Exception{
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
		JDBCAccountPersistenceServiceImpl service = new JDBCAccountPersistenceServiceImpl();
		service.cleanupFromUnitTests();
		return service;
	}

	//set to ignore to not break build, if you want to run this test locally create db pk_test on your local postgres or
	//alter pk-jdbc-account.json in test/appdata.
	@Test
	public void createAccountWithPSQL() throws Exception{
		createAccount(getService(PSQL));
	}

	@Test
	public void createAccountWithH2() throws Exception{
		createAccount(getService(H2));
	}

	public Account createAccount(JDBCAccountPersistenceServiceImpl service) throws Exception{
		Account toCreate = new Account();
		toCreate.setName("test");
		toCreate.setEmail("test@example.com");
		toCreate.setType(1);
		toCreate.setRegistrationTimestamp(System.currentTimeMillis());
		Account newAcc = Account.newAccountFromPattern(toCreate);
		service.saveAccount(newAcc);
		return newAcc;
	}

	@Test public void getAccountWithPSQL() throws Exception{
		testGetAccount(getService(PSQL));
	}
	@Test public void getAccountWithH2() throws Exception{
		testGetAccount(getService(H2));
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

	@Test public void deleteAccountWithPSQL() throws Exception{
		testDeleteAccount(getService(PSQL));
	}

	@Test public void deleteAccountWithH2() throws Exception{
		testDeleteAccount(getService(H2));
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
}
