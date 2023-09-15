package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceService;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Monitor(subsystem = "pushtoken", category = "portalkit-service")
public class PushTokenServiceImpl implements PushTokenService {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(PushTokenServiceImpl.class);

    /**
     * Persistence service.
     */
    @Autowired
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
            cache.remove(tokenOwner); // cache sync
            return tokenOwner;
        } catch (PushTokenPersistenceServiceException ex) {
            log.error("Cannot remove token", ex);
            throw new PushTokenServiceException("Cannot remove token", ex);
        }
    }

    @Override
    public void removeAllFromAccount(AccountId accountId) throws PushTokenServiceException {
        try {
            persistenceService.deleteAllFromAccount(accountId);

            // cache sync
            cache.remove(accountId);
        } catch (PushTokenPersistenceServiceException any) {
            log.error("Cannot remove tokens from account", any);
            throw new PushTokenServiceException("Cannot remove tokens from account", any);
        }
    }
}
