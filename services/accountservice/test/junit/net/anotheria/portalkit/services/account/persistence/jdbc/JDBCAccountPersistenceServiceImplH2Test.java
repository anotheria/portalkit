package net.anotheria.portalkit.services.account.persistence.jdbc;

import org.junit.Test;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.01.13 10:05
 */
public class JDBCAccountPersistenceServiceImplH2Test extends JDBCAccountPersistenceServiceImplTest{
	public static final String H2 = "h2";

	@Test
	public void testCreateAccountWithH2() throws Exception{
		createAccount(getService(H2));
	}

	@Test public void getAccountWithH2() throws Exception{
		testGetAccount(getService(H2));
	}

	@Test public void testDeleteAccountWithH2() throws Exception{
		testDeleteAccount(getService(H2));
	}

	@Test public void testEditAccountWithH2() throws Exception{
		testEditAccount(getService(H2));
	}


}
