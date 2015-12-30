package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Set to ignore to not break build, if you want to run this test locally create
 * db pk_test on your local postgres or alter pk-jdbc-account.json in
 * test/appdata.
 * 
 * @author lrosenberg
 * @since 15.01.13 10:02
 */
@Ignore
public class JDBCAccountPersistenceServiceImplPSQLTest extends JDBCAccountPersistenceServiceImplTest {

	/**
	 * 
	 */
	public static final String PSQL = "psql";

	@Test
	public void testCreateAccountWithPSQL() throws AccountPersistenceServiceException {
		createAccount(getService(PSQL));
	}

	@Test
	public void getAccountWithPSQL() throws AccountPersistenceServiceException {
		testGetAccount(getService(PSQL));
	}

	@Test
	public void testDeleteAccountWithPSQL() throws AccountPersistenceServiceException {
		testDeleteAccount(getService(PSQL));
	}

	@Test
	public void testEditAccountWithPSQL() throws AccountPersistenceServiceException {
		testEditAccount(getService(PSQL));
	}

	@Test
	public void testAccountFieldsWithPSQL() throws AccountPersistenceServiceException {
		testAccountFields(getService(PSQL));
	}

	@Test
	public void testGetByEmailOrName() throws AccountPersistenceServiceException {
		testGetByEmailAndGetByName(getService(PSQL));
	}

}
