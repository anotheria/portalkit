package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.12.12 10:17
 */
public class AuthenticationServiceTest {

	@Before @After
	public void setup(){
		MetaFactory.reset();
	}

	@Test
	public void testPassword() throws MetaFactoryException, AuthenticationServiceException, AuthenticationPersistenceServiceException{
		 AuthenticationService service = MetaFactory.get(AuthenticationService.class);

		AccountId id = new AccountId("FOO");
		String password = "abracadabra";
		service.setPassword(id, password);
		assertTrue(service.canAuthenticate(id, password));
		assertFalse(service.canAuthenticate(id, null));
		assertFalse(service.canAuthenticate(id, "another pass"));
		assertFalse(service.canAuthenticate(new AccountId("xxx"), null));
		assertFalse(service.canAuthenticate(new AccountId("xxx"), "another pass"));
		assertFalse(service.canAuthenticate(new AccountId("xxx"), password));

		try{
			assertFalse(service.canAuthenticate(null, password));
			fail("Expected exception");
		}catch(IllegalArgumentException e){}

		//check if the password is really stored
		AuthenticationPersistenceService persistenceService = MetaFactory.get(AuthenticationPersistenceService.class);
		assertNotNull(persistenceService.getEncryptedPassword(id));
		assertNull(persistenceService.getEncryptedPassword(new AccountId("xxx")));



	}
}
