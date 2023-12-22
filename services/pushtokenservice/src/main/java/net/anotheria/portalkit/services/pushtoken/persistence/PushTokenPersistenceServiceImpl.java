package net.anotheria.portalkit.services.pushtoken.persistence;

import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PushTokenPersistenceServiceImpl implements PushTokenPersistenceService {

    @Autowired
    private PushTokenRepository repository;

    /**
     * Lock manager.
     */
    private final IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<>();

    @Override
    public List<String> getAllTokensByAccountId(AccountId accountId) throws PushTokenPersistenceServiceException {
        try {
            return repository.findAllById_AccountId(accountId.getInternalId())
                    .stream().map(PushTokenEntity::getToken).collect(Collectors.toList());
        } catch (Exception any) {
            throw new PushTokenPersistenceServiceException(any.getMessage(), any);
        }
    }

    @Override
    public long getAllTokensCount() throws PushTokenPersistenceServiceException {
        return repository.count();
    }

    @Override
    public String saveTokenForAccount(AccountId accountId, String token) throws PushTokenPersistenceServiceException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            PushTokenEntity toSave = new PushTokenEntity(accountId.getInternalId(), token);
            repository.save(toSave);
            return token;
        } catch (Exception any) {
            throw new PushTokenPersistenceServiceException(any.getMessage(), any);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public AccountId deleteToken(String token) throws PushTokenPersistenceServiceException {
        try {
            PushTokenEntity toDelete = repository.getById_Token(token);
            if (toDelete != null) {
                IdBasedLock<AccountId> lock = lockManager.obtainLock(toDelete.getAccountId());
                lock.lock();
                try {
                    repository.delete(toDelete);
                    return toDelete.getAccountId();
                } finally {
                    lock.unlock();
                }
            }
            return null;
        } catch (Exception any) {
            throw new PushTokenPersistenceServiceException(any.getMessage(), any);
        }
    }

    @Override
    public void deleteAllFromAccount(AccountId accountId) throws PushTokenPersistenceServiceException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            List<PushTokenEntity> toDelete = repository.findAllById_AccountId(accountId.getInternalId());
            repository.deleteAll(toDelete);
        } catch (Exception any) {
            throw new PushTokenPersistenceServiceException(any.getMessage(), any);
        } finally {
            lock.unlock();
        }
    }
}
