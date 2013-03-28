package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;

import org.junit.Test;

/**
 * JDBCAccountPersistenceServiceImplH2Test.
 * 
 * @author lrosenberg
 * @since 15.01.13 10:05
 */
public class JDBCAccountPersistenceServiceImplH2Test extends JDBCAccountPersistenceServiceImplTest {
	/**
	 * 
	 */
	public static final String H2 = "h2";

	@Test
	public void testCreateAccountWithH2() throws AccountPersistenceServiceException {
		createAccount(getService(H2));
	}

	@Test
	public void getAccountWithH2() throws AccountPersistenceServiceException {
		testGetAccount(getService(H2));
	}

	@Test
	public void testDeleteAccountWithH2() throws AccountPersistenceServiceException {
		testDeleteAccount(getService(H2));
	}

	@Test
	public void testEditAccountWithH2() throws AccountPersistenceServiceException {
		testEditAccount(getService(H2));
	}

	@Test
	public void testAccountFieldsWithH2() throws AccountPersistenceServiceException {
		testAccountFields(getService(H2));
	}

	@Test
	public void testGetByEmailOrName() throws AccountPersistenceServiceException {
		testGetByEmailAndGetByName(getService(H2));
	}

}
