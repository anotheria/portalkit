package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceService;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Account settings service implementation.
 *
 * @author lrosenberg
 * @author dagafonov
 * @since 12.12.12 10:14
 */
public class AccountSettingsServiceImpl implements AccountSettingsService, EntityManagingService {
    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(AccountSettingsServiceImpl.class);

    /**
     * {@link AccountSettingsPersistenceService} instance.
     */
    private AccountSettingsPersistenceService persistence;

    /**
     * Lock manager.
     */
    private IdBasedLockManager<AccountId> accountsLockManager = new SafeIdBasedLockManager<AccountId>();

    /**
     * Cache for dataspaces.
     */
    private Cache<AccountId, DataspaceCacheHolder> cache;

    /**
     *
     */
    public AccountSettingsServiceImpl() {
        try {
            persistence = MetaFactory.get(AccountSettingsPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't instantiate persistence", e);
        }

        cache = Caches.createConfigurableHardwiredCache("pk-cache-account-settings-service");
        Caches.attachCacheToMoskitoLoggers(cache, "account-settings-cache", "cache", "portal-kit");
        EntityManagingServices.createEntityCounter(this, "");
    }

    @Override
    public int getEntityCount(String s) {
        try {
            return Long.valueOf(persistence.dataspacesCount()).intValue();
        } catch (AccountSettingsPersistenceServiceException e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public Dataspace getDataspace(AccountId accountId, DataspaceType domain) throws AccountSettingsServiceException {
        IdBasedLock<AccountId> lock = accountsLockManager.obtainLock(accountId);
        lock.lock();
        try {

            //first check cache
            DataspaceCacheHolder holder = cache.get(accountId);
            if (holder != null) {
                Dataspace fromCache = holder.get(domain.getId());
                if (fromCache != null)
                    return fromCache;
            }

            Dataspace ds = persistence.loadDataspace(accountId, domain.getId());
            if (ds == null) {
                throw new DataspaceNotFoundException(domain.getName());
            }
            putInCache(accountId, domain.getId(), ds);

            return ds;
        } catch (AccountSettingsPersistenceServiceException e) {
            throw new AccountSettingsServiceException("persistence.loadDataspace failed", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<Dataspace> getAllDataspaces(AccountId accountId) throws AccountSettingsServiceException {
        IdBasedLock<AccountId> lock = accountsLockManager.obtainLock(accountId);
        lock.lock();
        try {

            //first check cache
            DataspaceCacheHolder holder = cache.get(accountId);
            if (holder != null) {
                return new LinkedList<>(holder.getAll());
            }

            Collection<Dataspace> dataspaces = persistence.loadDataspaces(accountId);
            for (Dataspace dataspace : dataspaces) {
                putInCache(accountId, dataspace.getKey().getDataspaceId(), dataspace);
            }

            return dataspaces;
        } catch (AccountSettingsPersistenceServiceException e) {
            throw new AccountSettingsServiceException("persistence.loadDataspaces failed", e);
        } finally {
            lock.unlock();
        }
    }

    //this method is unsafe, it should be called from locked areas only.
    private void putInCache(AccountId accountId, int dataspaceType, Dataspace dataspace) {
        DataspaceCacheHolder holder = cache.get(accountId);
        if (holder == null) {
            holder = new DataspaceCacheHolder();
            cache.put(accountId, holder);
        }
        holder.put(dataspaceType, dataspace);
    }

    @Override
    public void saveDataspace(Dataspace dataspace) throws AccountSettingsServiceException {
        AccountId accId = new AccountId(dataspace.getKey().getAccountId());
        IdBasedLock<AccountId> lock = accountsLockManager.obtainLock(accId);
        lock.lock();
        try {
            persistence.saveDataspace(dataspace);
            putInCache(accId, dataspace.getKey().getDataspaceId(), dataspace);
        } catch (AccountSettingsPersistenceServiceException e) {
            throw new AccountSettingsServiceException("persistence.saveDataspace failed", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean deleteDataspace(AccountId accountId, DataspaceType dataspaceType) throws AccountSettingsServiceException {
        try {
            int type = dataspaceType.getId();
            boolean success = persistence.deleteDataspace(accountId, type);

            // remove dataspace from cache
            DataspaceCacheHolder holder = cache.get(accountId);

            if (holder.get(type) != null && success == true) {
                holder.remove(type);
            }

            return success;
        } catch (AccountSettingsPersistenceServiceException e) {
            throw new AccountSettingsServiceException("persistence failed ", e);
        }
    }

    @Override
    public boolean deleteDataspace(AccountId accountId, int dataspaceId) throws AccountSettingsServiceException {
        try {
            boolean success = persistence.deleteDataspace(accountId, dataspaceId);

            // remove dataspace from cache
            DataspaceCacheHolder holder = cache.get(accountId);

            if (holder.get(dataspaceId) != null && success) {
                holder.remove(dataspaceId);
            }

            return success;
        } catch (AccountSettingsPersistenceServiceException e) {
            throw new AccountSettingsServiceException("persistence failed ", e);
        }
    }

    @Override
    public int deleteDataspaces(AccountId accountId) throws AccountSettingsServiceException {
        try {
            return persistence.deleteDataspaces(accountId) ? 1 : 0;
        } catch (AccountSettingsPersistenceServiceException e) {
            throw new AccountSettingsServiceException("persistence failed ", e);
        }
    }

    @Override
    public void deleteData(AccountId accountId) {
        try {
            persistence.deleteDataspaces(accountId);
        } catch (AccountSettingsPersistenceServiceException e) {
            log.error("Deleting settings data for " + accountId + " failed.");
        }
    }
}
