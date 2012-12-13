package net.anotheria.portalkit.services.authentication.persistence.inmemory;

import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.12.12 10:06
 */
public class InMemoryAuthenticationPersistenceServiceImpl implements AuthenticationPersistenceService {

	private ConcurrentMap<AccountId, String> map = new ConcurrentHashMap<AccountId, String>();

	@Override
	public void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException {
		map.put(id, password);
	}

	@Override
	public String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		return map.get(id);
	}
}
