package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.authentication.persistence.inmemory.InMemoryAuthenticationPersistenceServiceImpl;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the token inventory: which tokens an account has, when they were created and when they were last used.
 *
 * @author lrosenberg
 * @since 04.09.26 10:00
 */
public class TokenInventoryTest {

	/**
	 * Token type used by the tests, the type an api key of an agent integration would have.
	 */
	private static final int AGENT_TYPE = 15;

	@Before @After
	public void setup(){
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());
		MetaFactory.addFactoryClass(AuthenticationService.class, Extension.LOCAL, AuthenticationServiceFactory.class);
		MetaFactory.addAlias(AuthenticationService.class, Extension.LOCAL);
	}

	@Test
	public void inventoryDescribesTheStoredToken() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		service.generateEncryptedToken(id, multiUseToken(id));

		List<TokenInventoryEntry> inventory = service.getTokenInventoryByAccount(id);
		assertEquals(1, inventory.size());

		TokenInventoryEntry entry = inventory.get(0);
		assertEquals(id, entry.getAccountId());
		assertEquals(AGENT_TYPE, entry.getType());
		assertTrue("the token was just created", entry.isCreatedKnown());
		assertFalse("it has not been used yet", entry.isUsed());
		assertEquals(TokenInventoryEntry.TIMESTAMP_UNKNOWN, entry.getLastUsed());
		assertTrue(entry.isMultiUse());
	}

	@Test
	public void inventoryNeverCarriesTheToken() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		EncryptedAuthToken encrypted = service.generateEncryptedToken(id, multiUseToken(id));
		String token = encrypted.getEncryptedVersion();

		TokenInventoryEntry entry = service.getTokenInventoryByAccount(id).get(0);
		assertFalse("the raw token must not be in the inventory", token.equals(entry.getObfuscatedToken()));
		assertEquals(TokenObfuscator.obfuscate(token), entry.getObfuscatedToken());
	}

	@Test
	public void authenticationMarksAMultiUseTokenAsUsed() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		EncryptedAuthToken encrypted = service.generateEncryptedToken(id, multiUseToken(id));
		assertFalse(service.getTokenInventoryByAccount(id).get(0).isUsed());

		service.authenticateByEncryptedToken(encrypted.getEncryptedVersion());

		TokenInventoryEntry entry = service.getTokenInventoryByAccount(id).get(0);
		assertTrue("the token was just authenticated with", entry.isUsed());
	}

	@Test
	public void probingDoesNotCountAsUsage() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		EncryptedAuthToken encrypted = service.generateEncryptedToken(id, multiUseToken(id));

		//canAuthenticate does not hand out the account id, so nothing can be done with its result and it is
		//deliberately not counted as a use of the token.
		assertTrue(service.canAuthenticateByEncryptedToken(encrypted.getEncryptedVersion()));

		assertFalse(service.getTokenInventoryByAccount(id).get(0).isUsed());
	}

	@Test
	public void consumedTokenLeavesTheInventory() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		AuthToken singleUse = multiUseToken(id);
		singleUse.setMultiUse(false);
		EncryptedAuthToken encrypted = service.generateEncryptedToken(id, singleUse);

		assertEquals(1, service.getTokenInventoryByAccount(id).size());
		service.authenticateByEncryptedToken(encrypted.getEncryptedVersion());

		//the inventory describes what exists, and a consumed single use token does not exist anymore.
		assertEquals(0, service.getTokenInventoryByAccount(id).size());
	}

	@Test
	public void inventoryByTypeIsBoundByLimitAndOffset() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);

		for (int i = 0; i < 5; i++){
			AccountId id = AccountId.generateNew();
			service.generateEncryptedToken(id, multiUseToken(id));
		}

		assertEquals(5, service.getTokenInventoryByType(AGENT_TYPE, 100, 0).size());
		assertEquals(2, service.getTokenInventoryByType(AGENT_TYPE, 2, 0).size());
		assertEquals(3, service.getTokenInventoryByType(AGENT_TYPE, 100, 2).size());
		assertEquals(0, service.getTokenInventoryByType(AGENT_TYPE, 100, 5).size());
		assertEquals(0, service.getTokenInventoryByType(AGENT_TYPE + 1, 100, 0).size());
	}

	@Test
	public void inventoryByTypeCannotBeCalledUnbounded() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		try {
			service.getTokenInventoryByType(AGENT_TYPE, 0, 0);
			fail("a limit of zero has to be rejected, there is no give me everything variant");
		} catch (IllegalArgumentException e) {
			//expected
		}
		try {
			service.getTokenInventoryByType(AGENT_TYPE, 10, -1);
			fail("a negative offset has to be rejected");
		} catch (IllegalArgumentException e) {
			//expected
		}
	}

	@Test
	public void lastUsedIsOnlyWrittenOncePerInterval() throws AuthenticationPersistenceServiceException {
		//tested against the persistence directly, that is where the throttling condition lives.
		AuthenticationPersistenceService persistence = new InMemoryAuthenticationPersistenceServiceImpl();
		AccountId id = AccountId.generateNew();

		AuthToken token = multiUseToken(id);
		EncryptedAuthToken encrypted = new EncryptedAuthToken();
		encrypted.setAuthToken(token);
		encrypted.setEncryptedVersion(AuthTokenEncryptors.encrypt(token));
		persistence.saveAuthToken(id, encrypted);

		String stored = encrypted.getEncryptedVersion();

		//unknown so far, so the first use is written in any case.
		persistence.updateLastUsed(stored, 1000L, 0L);
		assertEquals(1000L, lastUsedOf(persistence, id));

		//used again while the stored value is younger than the interval - no write, this is the whole point.
		persistence.updateLastUsed(stored, 2000L, 500L);
		assertEquals(1000L, lastUsedOf(persistence, id));

		//used again after the interval has passed.
		persistence.updateLastUsed(stored, 3000L, 1500L);
		assertEquals(3000L, lastUsedOf(persistence, id));
	}

	/**
	 * Returns the last used timestamp of the single token of the given account.
	 *
	 * @param persistence the persistence to ask.
	 * @param id          the account.
	 * @return the last used timestamp.
	 * @throws AuthenticationPersistenceServiceException if error.
	 */
	private long lastUsedOf(AuthenticationPersistenceService persistence, AccountId id) throws AuthenticationPersistenceServiceException {
		List<TokenInventoryEntry> inventory = persistence.getTokenInventoryByAccount(id);
		assertEquals(1, inventory.size());
		return inventory.get(0).getLastUsed();
	}

	/**
	 * Creates a long lived multi use token of the agent type, the shape an api key has.
	 *
	 * @param id owner of the token.
	 * @return the token.
	 */
	private AuthToken multiUseToken(AccountId id){
		AuthToken token = new AuthToken();
		token.setAccountId(id);
		token.setType(AGENT_TYPE);
		token.setMultiUse(true);
		token.setExpiryTimestamp(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365);
		return token;
	}
}
