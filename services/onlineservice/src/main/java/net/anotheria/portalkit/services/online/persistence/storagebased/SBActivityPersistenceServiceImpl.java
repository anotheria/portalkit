package net.anotheria.portalkit.services.online.persistence.storagebased;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.persistence.ActivityNotFoundInPersistenceServiceException;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceServiceException;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.exception.EntityNotFoundStorageException;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import net.anotheria.util.log.LogMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceConstants.ACTIVITY_PERSISTENCE_GENERIC_STORAGE_NAME;

/**
 * {@link StorageService} based {@link ActivityPersistenceService} implementation.
 *
 * @author h3llka
 */
public class SBActivityPersistenceServiceImpl implements ActivityPersistenceService {
    /**
     * Logging utility instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SBActivityPersistenceServiceImpl.class);
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<AccountId> lockManager;
    /**
     * {@link StorageService} instance.
     */
    private StorageService<AccountActivityVO> persistence;

    /**
     * Constructor.
     */
    @SuppressWarnings("unchecked")
    SBActivityPersistenceServiceImpl() {
        try {
            persistence = MetaFactory.get(StorageService.class, ACTIVITY_PERSISTENCE_GENERIC_STORAGE_NAME);
        } catch (MetaFactoryException e) {
            LOG.error("StorageService init failure", e);
        }
        lockManager = new SafeIdBasedLockManager<AccountId>();
    }

    @Override
    public long saveLastLogin(AccountId account, long lastLoginTime) throws ActivityPersistenceServiceException {
        if (account == null)
            throw new IllegalArgumentException("Incoming parameter account is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            //there is no need to read  persisted data currently.
            persistence.save(new AccountActivityVO(account, lastLoginTime, lastLoginTime));
            return lastLoginTime;
        } catch (StorageException e) {
            String failMsg = LogMessageUtil.failMsg(e, account, lastLoginTime);
            LOG.error(failMsg, e);
            throw new ActivityPersistenceServiceException(failMsg, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long saveLastActivity(AccountId account, long lastActivityTime) throws ActivityPersistenceServiceException {
        if (account == null)
            throw new IllegalArgumentException("Incoming parameter account is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            AccountActivityVO persisted = readFromStorage(account);
            if (persisted == null)
                throw new ActivityNotFoundInPersistenceServiceException(account);

            persisted.setLastActivityTime(lastActivityTime);
            //there is no need to read  persisted data currently.
            persistence.save(persisted);
            return lastActivityTime;
        } catch (StorageException e) {
            String failMsg = LogMessageUtil.failMsg(e, account, lastActivityTime);
            LOG.error(failMsg, e);
            throw new ActivityPersistenceServiceException(failMsg, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long readLastLogin(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            AccountActivityVO persisted = readFromStorage(accountId);
            if (persisted == null)
                throw new ActivityNotFoundInPersistenceServiceException(accountId);
            return persisted.getLastLoginTime();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long readLastActivity(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            AccountActivityVO persisted = readFromStorage(accountId);
            if (persisted == null)
                throw new ActivityNotFoundInPersistenceServiceException(accountId);
            return persisted.getLastActivityTime();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<AccountId, Long> readLastLogin(List<AccountId> accounts) throws ActivityPersistenceServiceException {
        if (accounts == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");
        Map<AccountId, Long> result = new HashMap<AccountId, Long>();
        if (accounts.isEmpty())
            return result;

        List<AccountActivityVO> entries = readFromStorage(accounts);
        for (AccountActivityVO entry : entries)
            result.put(entry.getAccountId(), entry.getLastLoginTime());

        return result;
    }

    @Override
    public Map<AccountId, Long> readLastActivity(List<AccountId> accounts) throws ActivityPersistenceServiceException {
        if (accounts == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");
        Map<AccountId, Long> result = new HashMap<AccountId, Long>();
        if (accounts.isEmpty())
            return result;
        List<AccountActivityVO> entries = readFromStorage(accounts);
        for (AccountActivityVO entry : entries)
            result.put(entry.getAccountId(), entry.getLastActivityTime());

        return result;

    }

    @Override
    public void deleteActivityEntry(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            persistence.delete(accountId.getInternalId());
        } catch (StorageException e) {
            final String msg = LogMessageUtil.failMsg(e, accountId);
            LOG.error(msg, e);
            throw new ActivityPersistenceServiceException(msg, e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Read {@link AccountActivityVO} with selected {@link AccountId} from the persistence.
     *
     * @param acc {@link AccountId}
     * @return {@link AccountActivityVO}
     * @throws ActivityPersistenceServiceException
     *          on errors from {@link StorageService}
     */
    private AccountActivityVO readFromStorage(final AccountId acc) throws ActivityPersistenceServiceException {
        try {
            return persistence.read(acc.toString());
        } catch (EntityNotFoundStorageException e) {
            return null;
        } catch (StorageException e) {
            String failMsg = LogMessageUtil.failMsg(e, acc);
            LOG.error(failMsg, e);
            throw new ActivityPersistenceServiceException(failMsg, e);
        }
    }


    /**
     * Read {@link AccountActivityVO} collection for selected {@link AccountId} collection from the persistence.
     *
     * @param accountIds {@link AccountId} collection
     * @return {@link AccountActivityVO}
     * @throws ActivityPersistenceServiceException
     *          on errors from {@link StorageService}
     */
    private List<AccountActivityVO> readFromStorage(final List<AccountId> accountIds) throws ActivityPersistenceServiceException {
        try {
            return persistence.read(toStringIdCollection(accountIds));
        } catch (StorageException e) {
            String failMsg = LogMessageUtil.failMsg(e, accountIds.size());
            LOG.error(failMsg, e);
            throw new ActivityPersistenceServiceException(failMsg, e);
        }
    }

    /**
     * Convert {@link AccountId} collection to {@link String} collection, using {@link AccountId#getInternalId()} as base value.
     *
     * @param source {@link AccountId} collection
     * @return {@link AccountId#getInternalId()} values
     */
    private static List<String> toStringIdCollection(final List<AccountId> source) {
        List<String> result = new ArrayList<String>();

        for (AccountId accountId : source)
            result.add(accountId.getInternalId());

        return result;
    }

    /**
     * Account activity value object, which will be delegated to persistence.
     */
    public static final class AccountActivityVO implements Serializable {

        /**
         * Basic serial version UID.
         */
        private static final long serialVersionUID = 6735177276406301236L;
        /**
         * AccountActivityVO 'accountId'.
         */
        private AccountId accountId;
        /**
         * AccountActivityVO 'lastLoginTime'.
         */
        private long lastLoginTime;
        /**
         * AccountActivityVO 'lastActivityTime'.
         */
        private long lastActivityTime;

        /**
         * Default constructor.
         */
        public AccountActivityVO() {
            this(null, 0L, 0L);
        }

        /**
         * Constructor.
         *
         * @param accountId {@link AccountId} as owner id
         */
        public AccountActivityVO(AccountId accountId) {
            this(accountId, 0L, 0L);
        }

        /**
         * Constructor.
         *
         * @param aAccountId        {@link AccountId}
         * @param aLastLoginTime    last login time stamp
         * @param aLastActivityTime last activity time stamp
         */
        public AccountActivityVO(AccountId aAccountId, long aLastLoginTime, long aLastActivityTime) {
            this.accountId = aAccountId;
            this.lastLoginTime = aLastLoginTime;
            this.lastActivityTime = aLastActivityTime;
        }

        public AccountId getAccountId() {
            return accountId;
        }

        public void setAccountId(AccountId accountId) {
            this.accountId = accountId;
        }

        public long getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(long lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public long getLastActivityTime() {
            return lastActivityTime;
        }

        public void setLastActivityTime(long lastActivityTime) {
            this.lastActivityTime = lastActivityTime;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof AccountActivityVO && AccountActivityVO.class.cast(o).getAccountId().equals(getAccountId());
        }

        @Override
        public int hashCode() {
            return accountId != null ? accountId.hashCode() : 0;
        }
    }

}
