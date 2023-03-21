package net.anotheria.portalkit.services.pushtoken.persistence.inmemory;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceService;
import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenPersistenceServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of PushTokenPersistenceService.
 */
public class InMemoryPushTokenPersistenceServiceImpl implements PushTokenPersistenceService {

    /**
     * Storage instance.
     */
    private ConcurrentHashMap<AccountId, List<String>> storage = new ConcurrentHashMap<>();

    /**
     * Lock manager.
     */
    private final IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<>();

    @Override
    public List<String> getAllTokensByAccountId(AccountId accountId) throws PushTokenPersistenceServiceException {
        if (accountId == null) {
            throw new PushTokenPersistenceServiceException("accountId is null");
        }
        return storage.get(accountId);
    }

    @Override
    public String saveTokenForAccount(AccountId accountId, String token) throws PushTokenPersistenceServiceException {
        if (accountId == null) {
            throw new PushTokenPersistenceServiceException("account id is null");
        }
        if (StringUtils.isEmpty(token)) {
            throw new PushTokenPersistenceServiceException("token is null or empty");
        }

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            List<String> accountTokens = storage.get(accountId);
            if (accountTokens == null) {
                accountTokens = new LinkedList<>();
            }
            accountTokens.add(token);
            storage.put(accountId, accountTokens);
            return token;
        } catch (Exception any) {
            throw new PushTokenPersistenceServiceException("Cannot save token for account", any);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public AccountId deleteToken(String token) throws PushTokenPersistenceServiceException {
        try {
            AccountId accountThatHasToken = null;
            for (Map.Entry<AccountId, List<String>> entry : storage.entrySet()) {
                if (entry.getValue().contains(token)) {
                    accountThatHasToken = entry.getKey();
                    break;
                }
            }
            if (accountThatHasToken != null) {
                IdBasedLock<AccountId> lock = lockManager.obtainLock(accountThatHasToken);
                lock.lock();
                try {
                    List<String> tokens = storage.get(accountThatHasToken);
                    tokens.remove(token);
                    storage.put(accountThatHasToken, tokens);
                } finally {
                    lock.unlock();
                }
            }
            return accountThatHasToken;
        } catch (Exception any) {
            throw new PushTokenPersistenceServiceException("Cannot delete a token", any);
        }
    }
}
