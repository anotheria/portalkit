package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:22
 */
public class JDBCAccountPersistenceServiceImplTest {

	private JDBCAccountPersistenceServiceImpl getService(String environment) throws Exception{
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
		JDBCAccountPersistenceServiceImpl service = new JDBCAccountPersistenceServiceImpl();
		service.cleanupFromUnitTests();
		return service;
	}

	//set to ignore to not break build, if you want to run this test locally create db pk_test on your local postgres or
	//alter pk-jdbc-account.json in test/appdata.
	@Test @Ignore
	public void createAccountWithPSQL() throws Exception{
		createAccount(getService("psql"));
	}

	@Test
	public void createAccountWithHSQL() throws Exception{
		createAccount(getService("hsqldb"));
	}

	public void createAccount(JDBCAccountPersistenceServiceImpl service) throws Exception{
		Account toCreate = new Account();
		toCreate.setName("test");
		toCreate.setEmail("test@example.com");
		toCreate.setType(0);
		toCreate.setRegistrationTimestamp(System.currentTimeMillis());
		service.saveAccount(Account.newAccountFromPattern(toCreate));
	}
}
