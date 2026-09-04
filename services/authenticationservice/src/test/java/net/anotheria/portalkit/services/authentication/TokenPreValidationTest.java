package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.authentication.persistence.inmemory.InMemoryAuthenticationPersistenceServiceImpl;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests that a token is validated from itself before the persistence is asked about it.
 *
 * The authenticated algorithms make the decryption a proof that the token was minted with the configured phrase,
 * so a forged token can be rejected without a database query. That is the point of encrypting tokens rather than
 * hashing them, and it only holds if the service actually checks in that order.
 *
 * @author lrosenberg
 * @since 04.09.26 12:00
 */
public class TokenPreValidationTest {

	@Before @After
	public void setup(){
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

		MetaFactory.addFactoryClass(AuthenticationPersistenceService.class, Extension.LOCAL, CountingPersistenceFactory.class);
		MetaFactory.addAlias(AuthenticationPersistenceService.class, Extension.LOCAL);
		MetaFactory.addFactoryClass(AuthenticationService.class, Extension.LOCAL, AuthenticationServiceFactory.class);
		MetaFactory.addAlias(AuthenticationService.class, Extension.LOCAL);

		CountingPersistence.lookups = 0;
	}

	@Test
	public void aForgedTokenIsRejectedWithoutAskingThePersistence() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);

		try {
			service.authenticateByEncryptedToken("A:thisIsNotAToken");
			fail("a token which does not decrypt must not authenticate");
		} catch (AuthTokenNotFoundException e) {
			//expected - a forgery is indistinguishable from an unknown token for the caller.
		}

		assertEquals("the persistence must not be touched for a token which is not authentic", 0, CountingPersistence.lookups);
	}

	@Test
	public void aTamperedTokenIsRejectedWithoutAskingThePersistence() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		String token = service.generateEncryptedToken(id, tokenFor(id, System.currentTimeMillis() + 60000L)).getEncryptedVersion();
		CountingPersistence.lookups = 0;

		String tampered = flipLastCharacter(token);
		try {
			service.authenticateByEncryptedToken(tampered);
			fail("a manipulated token must not authenticate");
		} catch (AuthTokenNotFoundException e) {
			//expected - the authentication tag does not match anymore.
		}

		assertEquals("the persistence must not be touched for a manipulated token", 0, CountingPersistence.lookups);
	}

	@Test
	public void anExpiredTokenIsRejectedWithoutAskingThePersistence() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		String token = service.generateEncryptedToken(id, tokenFor(id, System.currentTimeMillis() - 1000L)).getEncryptedVersion();
		CountingPersistence.lookups = 0;

		try {
			service.authenticateByEncryptedToken(token);
			fail("an expired token must not authenticate");
		} catch (AuthTokenExpiredException e) {
			//expected - the expiry is inside the token, so it needs no lookup either.
		}

		assertEquals("expiry is decided from the token itself", 0, CountingPersistence.lookups);
	}

	@Test
	public void canAuthenticateSaysNoToAForgeryWithoutAskingThePersistence() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);

		assertFalse(service.canAuthenticateByEncryptedToken("A:thisIsNotAToken"));
		assertEquals(0, CountingPersistence.lookups);
	}

	@Test
	public void aValidTokenIsStillCheckedAgainstThePersistence() throws Exception {
		AuthenticationService service = MetaFactory.get(AuthenticationService.class);
		AccountId id = AccountId.generateNew();

		String token = service.generateEncryptedToken(id, tokenFor(id, System.currentTimeMillis() + 60000L)).getEncryptedVersion();
		CountingPersistence.lookups = 0;

		assertEquals(id, service.authenticateByEncryptedToken(token));
		assertTrue("an authentic token still has to be looked up, that is what makes revocation work",
				CountingPersistence.lookups > 0);
	}

	/**
	 * Creates a multi use token with the given expiry.
	 *
	 * @param id     owner.
	 * @param expiry expiry timestamp.
	 * @return the token.
	 */
	private AuthToken tokenFor(AccountId id, long expiry){
		AuthToken token = new AuthToken();
		token.setAccountId(id);
		token.setType(15);
		token.setMultiUse(true);
		token.setExpiryTimestamp(expiry);
		return token;
	}

	/**
	 * Changes the last character of the given token, which invalidates the authentication tag.
	 *
	 * @param token the token.
	 * @return the manipulated token.
	 */
	private String flipLastCharacter(String token){
		char last = token.charAt(token.length() - 1);
		char replacement = last == 'a' ? 'b' : 'a';
		return token.substring(0, token.length() - 1) + replacement;
	}

	/**
	 * Persistence which counts how often it was asked whether a token exists, and delegates everything else.
	 */
	public static class CountingPersistence implements AuthenticationPersistenceService {

		/**
		 * Number of existence lookups since the counter was last reset.
		 */
		static volatile int lookups = 0;

		private final AuthenticationPersistenceService delegate = new InMemoryAuthenticationPersistenceServiceImpl();

		@Override
		public boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException {
			lookups++;
			return delegate.authTokenExists(encryptedToken);
		}

		@Override
		public void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException {
			delegate.saveEncryptedPassword(id, password);
		}

		@Override
		public String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
			return delegate.getEncryptedPassword(id);
		}

		@Override
		public void deleteEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
			delegate.deleteEncryptedPassword(id);
		}

		@Override
		public void saveAuthToken(AccountId owner, EncryptedAuthToken encryptedToken) throws AuthenticationPersistenceServiceException {
			delegate.saveAuthToken(owner, encryptedToken);
		}

		@Override
		public Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
			return delegate.getAuthTokens(owner);
		}

		@Override
		public void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
			delegate.deleteAuthTokens(owner);
		}

		@Override
		public void deleteAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
			delegate.deleteAuthToken(owner, encryptedToken);
		}

		@Override
		public long authTokensCount() throws AuthenticationPersistenceServiceException {
			return delegate.authTokensCount();
		}

		@Override
		public List<TokenInventoryEntry> getTokenInventoryByAccount(AccountId owner) throws AuthenticationPersistenceServiceException {
			return delegate.getTokenInventoryByAccount(owner);
		}

		@Override
		public List<TokenInventoryEntry> getTokenInventoryByType(int type, int limit, int offset) throws AuthenticationPersistenceServiceException {
			return delegate.getTokenInventoryByType(type, limit, offset);
		}

		@Override
		public void updateLastUsed(String encryptedToken, long timestamp, long onlyIfOlderThan) throws AuthenticationPersistenceServiceException {
			delegate.updateLastUsed(encryptedToken, timestamp, onlyIfOlderThan);
		}
	}

	/**
	 * Factory handing the counting persistence to the MetaFactory.
	 */
	public static class CountingPersistenceFactory implements ServiceFactory<AuthenticationPersistenceService> {
		@Override
		public AuthenticationPersistenceService create() {
			return new CountingPersistence();
		}
	}
}
