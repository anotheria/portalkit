package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceService;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PushTokenServiceImpl implements PushTokenService {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(PushTokenServiceImpl.class);

    /**
     * Persistence service.
     */
    private PushTokenPersistenceService persistenceService;

    /**
     * Cache by {@link AccountId}.
     */
    private Cache<AccountId, List<String>> cache;

    /**
     * Default Constructor.
     */
    public PushTokenServiceImpl() {
        cache = Caches.createHardwiredCache("pushtokenservice-cache");
        try {
            persistenceService = MetaFactory.get(PushTokenPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't start without persistence service ", e);
        }
    }

    @Override
    public List<String> getAllByAccountId(AccountId accountId) throws PushTokenServiceException {
        try {
            List<String> cached = cache.get(accountId);
            if (cached != null) {
                return cached;
            }

            List<String> fromPersistence = persistenceService.getAllTokensByAccountId(accountId);
            cache.put(accountId, fromPersistence);
            return fromPersistence;
        } catch (PushTokenPersistenceServiceException ex) {
            log.error("Cannot get tokens by account", ex);
            throw new PushTokenServiceException("Cannot get tokens by account", ex);
        }
    }

    @Override
    public String addTokenForAccount(AccountId accountId, String token) throws PushTokenServiceException {
        try {
            removeToken(token);
            String savedToken = persistenceService.saveTokenForAccount(accountId, token);

            // cache sync
            List<String> cached = cache.get(accountId);
            if (cached != null) {
                cached.add(token);
            }
            return savedToken;
        } catch (PushTokenPersistenceServiceException ex) {
            log.error("Cannot add token for account", ex);
            throw new PushTokenServiceException("Cannot add token for account", ex);
        }
    }

    @Override
    public AccountId removeToken(String token) throws PushTokenServiceException {
        try {
            AccountId tokenOwner = persistenceService.deleteToken(token);

            // cache sync
            List<String> cached = cache.get(tokenOwner);
            if (cached != null) {
                cached.remove(token);
            }

            return tokenOwner;
        } catch (PushTokenPersistenceServiceException ex) {
            log.error("Cannot remove token", ex);
            throw new PushTokenServiceException("Cannot remove token", ex);
        }
    }

    @Override
    public void removeAllFromAccount(AccountId accountId) throws PushTokenServiceException {
        List<String> tokens = getAllByAccountId(accountId);
        for (String token : tokens) {
            removeToken(token);
        }
    }
}
