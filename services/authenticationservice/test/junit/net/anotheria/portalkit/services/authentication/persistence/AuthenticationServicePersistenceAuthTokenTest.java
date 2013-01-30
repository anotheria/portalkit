package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthTokenEncryptors;
import net.anotheria.portalkit.services.authentication.persistence.inmemory.InMemoryAuthenticationPersistenceServiceImpl;
import net.anotheria.portalkit.services.authentication.persistence.jdbc.JDBCAuthenticationPersistenceServiceImpl;
import net.anotheria.portalkit.services.common.AccountId;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for auth token handling.
 *
 * @author lrosenberg
 * @since 30.01.13 09:23
 */
public class AuthenticationServicePersistenceAuthTokenTest {

	@Test
	public void testCreateGetDeleteTokenInMemory() throws AuthenticationPersistenceServiceException{
		testCreateGetDeleteToken(new InMemoryAuthenticationPersistenceServiceImpl());
	}

	@Test
	public void testCreateGetDeleteTokenJDBC() throws AuthenticationPersistenceServiceException, Exception{
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		JDBCAuthenticationPersistenceServiceImpl service = new JDBCAuthenticationPersistenceServiceImpl();
		service.cleanupFromUnitTests();
		testCreateGetDeleteToken(service);
	}

	private void testCreateGetDeleteToken(AuthenticationPersistenceService service) throws AuthenticationPersistenceServiceException{
		AccountId id = AccountId.generateNew();
		AuthToken token1 = createDummyAuthToken(id);
		AuthToken token2 = createDummyAuthToken(id);
		token2.setType(43);

		String t1 = AuthTokenEncryptors.encrypt(token1);
		String t2 = AuthTokenEncryptors.encrypt(token2);

		assertFalse(service.authTokenExists(t1));
		assertFalse(service.authTokenExists(t2));

		//store token 1
		service.saveAuthToken(id, t1);
		assertTrue(service.authTokenExists(t1));
		assertFalse(service.authTokenExists(t2));

		//this should
		service.saveAuthToken(id, t2);
		assertTrue(service.authTokenExists(t1));
		assertTrue(service.authTokenExists(t2));

		service.deleteAuthToken(id, t2);
		assertTrue(service.authTokenExists(t1));
		assertFalse(service.authTokenExists(t2));

		service.deleteAuthTokens(id);
		assertFalse(service.authTokenExists(t1));
		assertFalse(service.authTokenExists(t2));
	}

	private AuthToken createDummyAuthToken(AccountId accId){
		AuthToken token = new AuthToken();

		token.setMultiUse(true);
		token.setExpiryTimestamp(System.currentTimeMillis()+1000000L);
		token.setExclusive(true);
		token.setExclusiveInType(true);
		token.setAccountId(accId);
		token.setType(42);


		return token;
	}

}
