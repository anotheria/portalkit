package net.anotheria.portalkit.services.online.persistence.inmemory;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.persistence.ActivityNotFoundInPersistenceServiceException;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * InMemory implementation for {@link ActivityPersistenceService}.
 *
 * @author h3llka
 */
public class InMemoryActivityPersistenceServiceImpl implements ActivityPersistenceService {
    /**
     * Logging util instance.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryActivityPersistenceServiceImpl.class);

    /**
     * InMemory storage.
     */
    private ConcurrentMap<AccountId, InternalDataHolder> storage;
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<AccountId> lockManager;

    /**
     * Constructor.
     */
    protected InMemoryActivityPersistenceServiceImpl() {
        storage = new ConcurrentHashMap<AccountId, InternalDataHolder>();
        lockManager = new SafeIdBasedLockManager<AccountId>();
    }

    @Override
    public long saveLastLogin(AccountId account, long lastLoginTime) throws ActivityPersistenceServiceException {
        if (account == null)
            throw new IllegalArgumentException("Incoming parameter account is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            InternalDataHolder holder = storage.get(account);
            if (holder == null) {
                holder = new InternalDataHolder(account, lastLoginTime, lastLoginTime);
                storage.put(account, holder);
                return lastLoginTime;
            }
            holder.setLastLogin(lastLoginTime);
            holder.setLastActivity(lastLoginTime);

            return lastLoginTime;
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
            InternalDataHolder holder = storage.get(account);
            if (holder == null)
                throw new ActivityNotFoundInPersistenceServiceException(account);
            holder.setLastActivity(lastActivityTime);

            return lastActivityTime;
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
            InternalDataHolder holder = storage.get(accountId);
            if (holder == null)
                throw new ActivityNotFoundInPersistenceServiceException(accountId);
            return holder.getLastLogin();
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
            InternalDataHolder holder = storage.get(accountId);
            if (holder == null)
                throw new ActivityNotFoundInPersistenceServiceException(accountId);
            return holder.getLastActivity();
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
        for (AccountId account : accounts)
            try {
                result.put(account, readLastLogin(account));
            } catch (ActivityNotFoundInPersistenceServiceException e) {
                if (LOG.isTraceEnabled())
                    LOG.trace(e.getMessage());
            }

        return result;
    }

    @Override
    public Map<AccountId, Long> readLastActivity(List<AccountId> accounts) throws ActivityPersistenceServiceException {
        if (accounts == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");
        Map<AccountId, Long> result = new HashMap<AccountId, Long>();
        if (accounts.isEmpty())
            return result;
        for (AccountId account : accounts)
            try {
                result.put(account, readLastActivity(account));
            } catch (ActivityNotFoundInPersistenceServiceException e) {
                if (LOG.isTraceEnabled())
                    LOG.trace(e.getMessage());
            }
        return result;
    }

    @Override
    public void deleteActivityEntry(AccountId accountId) throws ActivityPersistenceServiceException {
        if (accountId == null)
            throw new IllegalArgumentException("Incoming parameter accountId is not valid");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            storage.remove(accountId);
        } finally {
            lock.unlock();
        }
    }


    /**
     * Internal object for inMemory information storing.
     */
    private final class InternalDataHolder implements Serializable {

        /**
         * Basic serial version UID.
         */
        private static final long serialVersionUID = -4840382363185115071L;
        /**
         * Owner id.
         */
        private AccountId accountId;
        /**
         * Last login time.
         */
        private long lastLogin;
        /**
         * LAst activity time.
         */
        private long lastActivity;

        /**
         * Constructor.
         *
         * @param accountId    {@link AccountId} owner id
         * @param lastLogin    time stamp of last login
         * @param lastActivity time stamp of last activity
         */
        private InternalDataHolder(AccountId accountId, long lastLogin, long lastActivity) {
            this.accountId = accountId;
            this.lastLogin = lastLogin;
            this.lastActivity = lastActivity;
        }

        public AccountId getAccountId() {
            return accountId;
        }

        public long getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(long lastLogin) {
            this.lastLogin = lastLogin;
        }

        public long getLastActivity() {
            return lastActivity;
        }

        public void setLastActivity(long lastActivity) {
            this.lastActivity = lastActivity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InternalDataHolder that = (InternalDataHolder) o;

            if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return accountId != null ? accountId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "InternalDataHolder{" +
                    "accountId=" + accountId +
                    ", lastLogin=" + lastLogin +
                    ", lastActivity=" + lastActivity +
                    '}';
        }
    }
}
