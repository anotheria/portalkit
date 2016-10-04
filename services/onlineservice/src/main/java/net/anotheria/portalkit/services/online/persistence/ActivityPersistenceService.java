package net.anotheria.portalkit.services.online.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;
import java.util.Map;

/**
 * Persistence service for handling 'user#lastLogin' and 'user#lastActivity' properties, from {@link net.anotheria.portalkit.services.online.OnlineService}.
 *
 * @author h3llka
 */
public interface ActivityPersistenceService extends Service {
    /**
     * Save lastLogin property for selected {@link AccountId}.
     * On lastLogin save - lastActivity will be updated immediately.
     *
     * @param account {@link AccountId}
     * @param lastLoginTime     last login time
     * @return lastLoginTime  which was just saved
     * @throws ActivityPersistenceServiceException
     *          on errors
     */
    long saveLastLogin(final AccountId account, final long lastLoginTime) throws ActivityPersistenceServiceException;

    /**
     * Save lastActivity property for selected {@link AccountId}.
     *
     * @param account          {@link AccountId}
     * @param lastActivityTime time of last login
     * @return lastActivityTime which was just saved
     * @throws ActivityPersistenceServiceException
     *          on errors, {@link ActivityNotFoundInPersistenceServiceException} in case when no activity present in the persistence
     */
    long saveLastActivity(final AccountId account, final long lastActivityTime) throws ActivityPersistenceServiceException;

    /**
     * Return lastLogin for selected {@link AccountId}.
     *
     * @param accountId {@link AccountId}
     * @return last login property
     * @throws ActivityPersistenceServiceException
     *          on errors, {@link ActivityNotFoundInPersistenceServiceException} in case when no activity present in the persistence
     */
    long readLastLogin(final AccountId accountId) throws ActivityPersistenceServiceException;

    /**
     * Return lastActivity for selected {@link AccountId}.
     *
     * @param accountId {@link AccountId}
     * @return last activity property
     * @throws ActivityPersistenceServiceException
     *          on errors, {@link ActivityNotFoundInPersistenceServiceException} in case when no activity present in the persistence
     */
    long readLastActivity(final AccountId accountId) throws ActivityPersistenceServiceException;

    /**
     * Return lastLogin for selected {@link AccountId} collection.
     * If property does not exists for some account - it will not present in results.
     *
     * @param accounts {@link AccountId} collection
     * @return last activity property
     * @throws ActivityPersistenceServiceException
     *          on errors
     */
    Map<AccountId, Long> readLastLogin(final List<AccountId> accounts) throws ActivityPersistenceServiceException;

    /**
     * Return lastActivity for selected {@link AccountId} collection.
     * If property does not exists for some account - it will not present in results.
     *
     * @param accounts {@link AccountId} collection
     * @return last activity property
     * @throws ActivityPersistenceServiceException
     *          on errors
     */
    Map<AccountId, Long> readLastActivity(final List<AccountId> accounts) throws ActivityPersistenceServiceException;

    /**
     * Remove persisted activity entry for selected {@link AccountId}.
     *
     * @param accountId {@link AccountId}
     * @throws ActivityPersistenceServiceException
     *          on errors
     */
    void deleteActivityEntry(final AccountId accountId) throws ActivityPersistenceServiceException;
}
