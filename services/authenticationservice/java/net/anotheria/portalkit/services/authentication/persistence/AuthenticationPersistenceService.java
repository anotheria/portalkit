package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * The AuthenticationPersistenceService manages the encrypted password and the auth tokens.
 *
 * @author lrosenberg
 * @since 12.12.12 22:59
 */
public interface AuthenticationPersistenceService extends Service {
	void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException;

	String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException;


}
