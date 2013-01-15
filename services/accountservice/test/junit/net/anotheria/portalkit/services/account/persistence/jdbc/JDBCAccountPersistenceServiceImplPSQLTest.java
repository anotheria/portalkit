package net.anotheria.portalkit.services.account.persistence.jdbc;

import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO comment this class
 *    set to ignore to not break build, if you want to run this test locally create db pk_test on your local postgres or
 *    alter pk-jdbc-account.json in test/appdata.
 * @author lrosenberg
 * @since 15.01.13 10:02
 */
@Ignore
public class JDBCAccountPersistenceServiceImplPSQLTest extends JDBCAccountPersistenceServiceImplTest{
	public static final String PSQL = "psql";

	@Test
	public void testCreateAccountWithPSQL() throws Exception{
		createAccount(getService(PSQL));
	}

	@Test public void getAccountWithPSQL() throws Exception{
		testGetAccount(getService(PSQL));
	}

	@Test public void testDeleteAccountWithPSQL() throws Exception{
		testDeleteAccount(getService(PSQL));
	}

	@Test public void testEditAccountWithPSQL() throws Exception{
		testEditAccount(getService(PSQL));
	}

}
