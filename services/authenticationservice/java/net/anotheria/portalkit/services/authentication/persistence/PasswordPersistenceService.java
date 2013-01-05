package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * The PasswordPersistenceService manages the encrypted password and the auth tokens.
 *
 * @author lrosenberg
 * @since 12.12.12 22:59
 */
public interface PasswordPersistenceService extends Service {
	void saveEncryptedPassword(AccountId id, String password) throws PasswordPersistenceServiceException;

	String getEncryptedPassword(AccountId id) throws PasswordPersistenceServiceException;


}
