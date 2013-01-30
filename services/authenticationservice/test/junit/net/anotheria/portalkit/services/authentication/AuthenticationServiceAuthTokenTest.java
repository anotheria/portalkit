package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This is the test for the auth token handling part of the authentication service.
 *
 * @author lrosenberg
 * @since 29.01.13 15:20
 */
public class AuthenticationServiceAuthTokenTest {

	@Before
	@After
	public void setup(){
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
	}


	//this test test if one is able to login with the app token.
	@Test public void testBasicFunctionality() throws Exception{
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId accId = AccountId.generateNew();
		AuthToken tokenTemplate = createDummyAuthToken(accId);

		EncryptedAuthToken token = service.generateEncryptedToken(accId, tokenTemplate);
		assertNotNull("Token shouldn't be null", token);

		//Now try to authenticate with this token.
		AccountId id = service.authenticateByEncryptedToken(token.getEncryptedVersion());
		assertEquals(accId, id);
	}

	@Test public void testMultiUse() throws Exception{
		AccountId myId = AccountId.generateNew();
		AuthToken t1 = createDummyAuthToken(myId);
		t1.setType(42);t1.setExclusive(false);t1.setExclusiveInType(false);t1.setMultiUse(false);
		AuthToken t2 = createDummyAuthToken(myId);
		t2.setType(43);t2.setExclusive(false);t2.setExclusiveInType(false);t2.setMultiUse(true);

		String tt1, tt2;
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		tt1 = service.generateEncryptedToken(myId, t1).getEncryptedVersion();
		tt2 = service.generateEncryptedToken(myId, t2).getEncryptedVersion();

		assertTrue(service.canAuthenticateByEncryptedToken(tt1));
		assertTrue(service.canAuthenticateByEncryptedToken(tt2));

		//now use both tokens.
		assertEquals(myId, service.authenticateByEncryptedToken(tt1));
		assertEquals(myId, service.authenticateByEncryptedToken(tt2));

		//first token should be inactive now
		assertFalse(service.canAuthenticateByEncryptedToken(tt1));
		assertTrue(service.canAuthenticateByEncryptedToken(tt2));

		//retry authentication
		assertEquals(myId, service.authenticateByEncryptedToken(tt2));

		try{
			service.authenticateByEncryptedToken(tt1);
			fail("Expected auth token not found exception");
		}catch(AuthTokenNotFoundException e){

		}


	}

	@Test public void testExpiry() throws Exception{
		AccountId myId = AccountId.generateNew();
		AuthToken t1 = createDummyAuthToken(myId);
		t1.setType(42);
		t1.setExpiryTimestamp(System.currentTimeMillis()-1000);
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		String tt1 = service.generateEncryptedToken(myId, t1).getEncryptedVersion();

		assertFalse(service.canAuthenticateByEncryptedToken(tt1));
		try{
			service.authenticateByEncryptedToken(tt1);
			fail("AuthTokenExpiredException expected");
		}catch(AuthTokenExpiredException e){

		}
	}

	@Test public void testExclusiveInType() throws Exception{
		AccountId myId = AccountId.generateNew();
		AuthToken t1 = createDummyAuthToken(myId);
		t1.setType(42);
		AuthToken t2 = createDummyAuthToken(myId);
		t2.setType(43);
		AuthToken t3 = createDummyAuthToken(myId);
		t3.setType(42); t3.setExclusiveInType(true);


		String tt1, tt2, tt3;
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		tt1 = service.generateEncryptedToken(myId, t1).getEncryptedVersion();
		tt2 = service.generateEncryptedToken(myId, t2).getEncryptedVersion();

		assertTrue(service.canAuthenticateByEncryptedToken(tt1));
		assertTrue(service.canAuthenticateByEncryptedToken(tt2));

		//now create a new token which is exclusive in type and should render tt1 inactive.
		tt3 = service.generateEncryptedToken(myId, t3).getEncryptedVersion();
		assertFalse(service.canAuthenticateByEncryptedToken(tt1));
		assertTrue(service.canAuthenticateByEncryptedToken(tt2));
		assertTrue(service.canAuthenticateByEncryptedToken(tt3));


	}

	@Test public void testExclusive() throws Exception{
		AccountId myId = AccountId.generateNew();
		AuthToken t1 = createDummyAuthToken(myId);
		t1.setType(42);
		AuthToken t2 = createDummyAuthToken(myId);
		t2.setType(43);
		AuthToken t3 = createDummyAuthToken(myId);
		t3.setType(42); t3.setExclusive(true);


		String tt1, tt2, tt3;
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		tt1 = service.generateEncryptedToken(myId, t1).getEncryptedVersion();
		tt2 = service.generateEncryptedToken(myId, t2).getEncryptedVersion();

		assertTrue("Should be able to authenticate with my token ", service.canAuthenticateByEncryptedToken(tt1));
		assertTrue(service.canAuthenticateByEncryptedToken(tt2));

		//now create a new token which is exclusive in type and should render tt1 inactive.
		tt3 = service.generateEncryptedToken(myId, t3).getEncryptedVersion();
		assertFalse(service.canAuthenticateByEncryptedToken(tt1));
		assertFalse(service.canAuthenticateByEncryptedToken(tt2));  //difference to testExclusive in type test.
		assertTrue(service.canAuthenticateByEncryptedToken(tt3));


	}


	private AuthToken createDummyAuthToken(){
		return createDummyAuthToken(AccountId.generateNew());
	}

	private AuthToken createDummyAuthToken(AccountId id){
		AuthToken token = new AuthToken();

		token.setMultiUse(false);
		token.setExpiryTimestamp(System.currentTimeMillis()+1000000L);
		token.setExclusive(false);
		token.setExclusiveInType(false);
		token.setType(42);
		token.setAccountId(id);


		return token;
	}
}
