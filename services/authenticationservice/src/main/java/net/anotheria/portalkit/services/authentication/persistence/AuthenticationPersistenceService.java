package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.authentication.EncryptedAuthToken;
import net.anotheria.portalkit.services.common.AccountId;

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
	void saveAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException;

	void saveAuthTokenAdditional(AccountId owner, EncryptedAuthToken encryptedToken) throws AuthenticationPersistenceServiceException;

	Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException;

	boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException;

	void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException;

	void deleteAuthToken(AccountId owner, String encryptedToken)  throws AuthenticationPersistenceServiceException;

	long authTokensCount() throws AuthenticationPersistenceServiceException;

}
