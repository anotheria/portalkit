package net.anotheria.portalkit.apis.online;

import net.anotheria.anoplass.api.API;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;
import java.util.Map;

/**
 * Online User API interface.
 * Allow to query online account statuses, and  track accounts activities.
 * Provides base integration with {@link net.anotheria.portalkit.services.online.OnlineService}.
 *
 * @author h3llka
 */
public interface OnlineAPI extends API {
    /**
     * Return {@code true} if and only if - account with selected {@link AccountId} is online.
     *
     * @param accountId {@link AccountId}
     * @return boolean value
     */
    boolean isOnline(final AccountId accountId) throws OnlineAPIException;

    /**
     * Return currently online {@link AccountId}, which matches selected criteria.
     *
     * @param criteria {@link OnlineAccountReadCriteriaAO}
     * @return online {@link AccountId}
     * @throws OnlineAPIException on errors
     */
    List<AccountId> readOnlineAccounts(final OnlineAccountReadCriteriaAO criteria) throws OnlineAPIException;

    /**
     * Return #lastLogin timestamp for selected {@link AccountId}.
     *
     * @param account {@link AccountId}
     * @return last login time
     * @throws OnlineAPIException on errors
     */
    long readLastLoginTime(final AccountId account) throws OnlineAPIException;

    /**
     * Return #lastActivity timestamp for selected {@link AccountId}.
     *
     * @param account {@link AccountId}
     * @return last activity time
     * @throws OnlineAPIException on errors
     */
    long readLastActivityTime(final AccountId account) throws OnlineAPIException;

    /**
     * Return AccountId to lastLogin time mapping, for provided {@link AccountId} collection.
     *
     * @param accounts {@link AccountId} collection
     * @return {@link AccountId} - last login time mapping
     * @throws OnlineAPIException on errors
     */
    Map<AccountId, Long> readLastLoginTime(final List<AccountId> accounts) throws OnlineAPIException;

    /**
     * Return AccountId to lastActivity time mapping, for provided {@link AccountId} collection.
     *
     * @param accounts {@link AccountId} collection
     * @return {@link AccountId} - last activity time mapping
     * @throws OnlineAPIException on errors
     */
    Map<AccountId, Long> readLastActivityTime(final List<AccountId> accounts) throws OnlineAPIException;
}
