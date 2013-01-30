package net.anotheria.portalkit.services.authentication.persistence.inmemory;

import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

	private Set<String> authTokensSet = Collections.synchronizedSet(new HashSet<String>());

	private ConcurrentHashMap<AccountId, Set<String>> authTokensByAccount = new ConcurrentHashMap<AccountId, Set<String>>();

	@Override
	public void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException {
		System.out.println("Saving password "+id+" = "+password);
		map.put(id, password);
	}

	@Override
	public String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		return map.get(id);
	}

	@Override
	public void deleteEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		map.remove(id);
	}

	@Override
	public void saveAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		Set<String> accountsTokens = authTokensByAccount.get(owner);
		if (accountsTokens==null){
			Set<String> newSet = Collections.synchronizedSet(new HashSet<String>());
			Set<String> oldSet = authTokensByAccount.putIfAbsent(owner, newSet);
			accountsTokens = oldSet == null ? newSet : oldSet;
		}
		accountsTokens.add(encryptedToken);
		authTokensSet.add(encryptedToken);
	}

	@Override
	public Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		Set<String> set = authTokensByAccount.get(owner);
		if (set==null)
			return Collections.<String>emptySet();
		HashSet<String> ret = new HashSet<String>();
		ret.addAll(set);
		return ret;
	}

	@Override
	public boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException {
		return authTokensSet.contains(encryptedToken);
	}

	@Override
	public void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		Set<String> accountsTokens = authTokensByAccount.get(owner);
		if (accountsTokens==null)
			return;
		for (Iterator<String> it = accountsTokens.iterator(); it.hasNext();){
			authTokensSet.remove(it.next());
		}
		accountsTokens.clear();
	}

	@Override
	public void deleteAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		Set<String> accountsTokens = authTokensByAccount.get(owner);
		if (accountsTokens==null)
			return;
		accountsTokens.remove(encryptedToken);
		authTokensSet.remove(encryptedToken);
	}
}
