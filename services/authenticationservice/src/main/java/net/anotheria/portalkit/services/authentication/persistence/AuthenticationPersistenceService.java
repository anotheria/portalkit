package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.authentication.EncryptedAuthToken;
import net.anotheria.portalkit.services.authentication.TokenInventoryEntry;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;
import java.util.Set;

/**
 * The AuthenticationPersistenceService manages the encrypted password and the auth tokens.
 *
 * @author lrosenberg
 * @since 12.12.12 22:59
 */
public interface AuthenticationPersistenceService extends Service {
	void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException;

	String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException;

	void deleteEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException;


	// the interface part for auth token handling. it will probably be subject of change in the future.

	/**
	 * Stores the token together with its properties. The properties are stored as own attributes and not only as
	 * part of the encrypted token, so that the token inventory can be built without decrypting anything.
	 *
	 * @param owner          owner of the token.
	 * @param encryptedToken the token and its properties.
	 * @throws AuthenticationPersistenceServiceException if error.
	 */
	void saveAuthToken(AccountId owner, EncryptedAuthToken encryptedToken) throws AuthenticationPersistenceServiceException;

	Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException;

	boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException;

	void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException;

	void deleteAuthToken(AccountId owner, String encryptedToken)  throws AuthenticationPersistenceServiceException;

	long authTokensCount() throws AuthenticationPersistenceServiceException;

	/**
	 * Returns the inventory of all tokens of the given owner. The entries carry obfuscated tokens only.
	 *
	 * @param owner owner of the tokens.
	 * @return the inventory entries, never null.
	 * @throws AuthenticationPersistenceServiceException if error.
	 */
	List<TokenInventoryEntry> getTokenInventoryByAccount(AccountId owner) throws AuthenticationPersistenceServiceException;

	/**
	 * Returns the inventory of tokens of the given type. The result is bound by limit on purpose, a token type
	 * which is used for logins matches millions of rows in a large installation.
	 *
	 * @param type   the token type.
	 * @param limit  maximum number of entries to return.
	 * @param offset number of entries to skip.
	 * @return the inventory entries, never null.
	 * @throws AuthenticationPersistenceServiceException if error.
	 */
	List<TokenInventoryEntry> getTokenInventoryByType(int type, int limit, int offset) throws AuthenticationPersistenceServiceException;

	/**
	 * Sets the last used timestamp of the given token, but only if the stored value is older than the given
	 * threshold. The condition is part of this call and not of the caller, so that a busy token costs one
	 * conditional update instead of a read followed by a write.
	 *
	 * @param encryptedToken  the token which was used.
	 * @param timestamp       the timestamp to store.
	 * @param onlyIfOlderThan store only if the currently stored value is unknown or older than this.
	 * @throws AuthenticationPersistenceServiceException if error.
	 */
	void updateLastUsed(String encryptedToken, long timestamp, long onlyIfOlderThan) throws AuthenticationPersistenceServiceException;

}
