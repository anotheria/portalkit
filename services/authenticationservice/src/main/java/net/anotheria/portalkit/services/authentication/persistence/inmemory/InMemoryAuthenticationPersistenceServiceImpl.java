package net.anotheria.portalkit.services.authentication.persistence.inmemory;

import net.anotheria.portalkit.services.authentication.EncryptedAuthToken;
import net.anotheria.portalkit.services.authentication.TokenInventoryEntry;
import net.anotheria.portalkit.services.authentication.TokenObfuscator;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

	/**
	 * Properties of the stored tokens, keyed by the token itself. Kept next to the sets above because the token
	 * inventory needs the properties of a token and not only its existence.
	 */
	private ConcurrentMap<String, StoredToken> tokenProperties = new ConcurrentHashMap<String, StoredToken>();

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
	public void saveAuthToken(AccountId owner, EncryptedAuthToken encryptedToken) throws AuthenticationPersistenceServiceException {
		Set<String> accountsTokens = authTokensByAccount.get(owner);
		if (accountsTokens==null){
			Set<String> newSet = Collections.synchronizedSet(new HashSet<String>());
			Set<String> oldSet = authTokensByAccount.putIfAbsent(owner, newSet);
			accountsTokens = oldSet == null ? newSet : oldSet;
		}
		String token = encryptedToken.getEncryptedVersion();
		accountsTokens.add(token);
		authTokensSet.add(token);
		tokenProperties.put(token, new StoredToken(owner, encryptedToken, System.currentTimeMillis()));
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
			String token = it.next();
			authTokensSet.remove(token);
			tokenProperties.remove(token);
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
		tokenProperties.remove(encryptedToken);
	}

	@Override
	public long authTokensCount() throws AuthenticationPersistenceServiceException {
		return authTokensByAccount.size();
	}

	@Override
	public List<TokenInventoryEntry> getTokenInventoryByAccount(AccountId owner) throws AuthenticationPersistenceServiceException {
		List<TokenInventoryEntry> ret = new ArrayList<TokenInventoryEntry>();
		Set<String> accountsTokens = authTokensByAccount.get(owner);
		if (accountsTokens == null)
			return ret;

		for (String token : getAuthTokens(owner)){
			StoredToken stored = tokenProperties.get(token);
			if (stored != null)
				ret.add(stored.toInventoryEntry());
		}
		return ret;
	}

	@Override
	public List<TokenInventoryEntry> getTokenInventoryByType(int type, int limit, int offset) throws AuthenticationPersistenceServiceException {
		List<TokenInventoryEntry> matching = new ArrayList<TokenInventoryEntry>();
		for (StoredToken stored : tokenProperties.values()){
			if (stored.getType() == type)
				matching.add(stored.toInventoryEntry());
		}

		if (offset >= matching.size())
			return new ArrayList<TokenInventoryEntry>();

		int to = offset + limit;
		if (to > matching.size() || to < 0)
			to = matching.size();
		return new ArrayList<TokenInventoryEntry>(matching.subList(offset, to));
	}

	@Override
	public void updateLastUsed(String encryptedToken, long timestamp, long onlyIfOlderThan) throws AuthenticationPersistenceServiceException {
		StoredToken stored = tokenProperties.get(encryptedToken);
		if (stored == null)
			return;
		stored.setLastUsedIfOlderThan(timestamp, onlyIfOlderThan);
	}

	/**
	 * Everything which is known about one stored token. The jdbc and mongo backends keep this in columns resp.
	 * fields, in memory it has to be held explicitly.
	 */
	private static class StoredToken {
		private final AccountId owner;
		private final EncryptedAuthToken token;
		private final long created;
		private volatile long lastUsed = TokenInventoryEntry.TIMESTAMP_UNKNOWN;

		StoredToken(AccountId owner, EncryptedAuthToken token, long created){
			this.owner = owner;
			this.token = token;
			this.created = created;
		}

		int getType(){
			return token.getAuthToken().getType();
		}

		synchronized void setLastUsedIfOlderThan(long timestamp, long onlyIfOlderThan){
			if (lastUsed == TokenInventoryEntry.TIMESTAMP_UNKNOWN || lastUsed < onlyIfOlderThan)
				lastUsed = timestamp;
		}

		TokenInventoryEntry toInventoryEntry(){
			return new TokenInventoryEntry(owner, token.getAuthToken().getType(),
					TokenObfuscator.obfuscate(token.getEncryptedVersion()), created, lastUsed,
					token.getAuthToken().getExpiryTimestamp(), token.getAuthToken().isMultiUse(),
					token.getAuthToken().isExclusive(), token.getAuthToken().isExclusiveInType());
		}
	}
}
