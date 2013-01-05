package net.anotheria.portalkit.services.authentication.persistence.inmemory;

import net.anotheria.portalkit.services.authentication.persistence.PasswordPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.PasswordPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.12.12 10:06
 */
public class InMemoryPasswordPersistenceServiceImpl implements PasswordPersistenceService {

	private ConcurrentMap<AccountId, String> map = new ConcurrentHashMap<AccountId, String>();

	@Override
	public void saveEncryptedPassword(AccountId id, String password) throws PasswordPersistenceServiceException {
		System.out.println("Saving password "+id+" = "+password);
		map.put(id, password);
	}

	@Override
	public String getEncryptedPassword(AccountId id) throws PasswordPersistenceServiceException {
		return map.get(id);
	}
}
