package net.anotheria.portalkit.services.online;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;
import java.util.Map;

/**
 * Online service interface.
 *
 * @author h3llka
 */
@DistributeMe(
        initcode = {
                "net.anotheria.portalkit.services.online.OnlineServiceConfigurator.configure();"
        }
)
@FailBy(strategyClass=RetryCallOnce.class)
public interface OnlineService extends Service {
    /**
     * Notify current service that some account  with {@link AccountId} was logged in.
     * During current operation Account - last login date will be triggered to currentTime.
     *
     * @param account {@link AccountId}
     * @throws OnlineServiceException on errors, {@link AccountIsOnlineException} in case if such account is online, or still online.
     */
    void notifyLoggedIn(final AccountId account) throws OnlineServiceException;

    /**
     * Notify service  that  user is  currently active and should not  be removed from online users storage by auto time task.
     *
     * @param account {@link AccountId}
     * @throws OnlineServiceException on errors, {@link AccountIsOfflineException} in case when maintenance operation failed cause account is not online
     */
    void notifyUserActivity(final AccountId account) throws OnlineServiceException;

    /**
     * Notify current service that some account  with {@link AccountId} was logged out.
     *
     * @param accountId {@link AccountId}
     * @throws OnlineServiceException on errors, {@link AccountIsOfflineException} in case if user is already offline
     */
    void notifyLoggedOut(final AccountId accountId) throws OnlineServiceException;

    /**
     * Return {@code true} in case if account with provided {@link AccountId} is currently logged in (online).
     *
     * @param accountId {@link AccountId}
     * @return {@link Boolean} value
     * @throws OnlineServiceException on errors
     */
    boolean isOnline(final AccountId accountId) throws OnlineServiceException;

    /**
     * Return {@link AccountId} collection, with accounts which are currently online (loggedIn).
     * Incoming {@link OnlineAccountReadCriteria} contains {@link AccountId} collection which  will be used as source collection for online/offline check,
     * if some {@link AccountId} is not online, it won't present in result collection.
     *
     * @param criteria {@link OnlineAccountReadCriteria} with read properties
     * @return {@link AccountId} collection which contains only online acc data
     * @throws OnlineServiceException on errors
     */
    List<AccountId> readOnlineUsers(final OnlineAccountReadCriteria criteria) throws OnlineServiceException;

    /**
     * Return last login time stamp for selected {@link AccountId}.
     *
     * @param accountId {@link AccountId}
     * @return last login time
     * @throws OnlineServiceException on errors
     */
    long readLastLogin(final AccountId accountId) throws OnlineServiceException;

    /**
     * Return last activity time stamp for selected {@link AccountId}.
     *
     * @param accountId {@link AccountId}
     * @return last activity time
     * @throws OnlineServiceException on errors
     */
    long readLastActivity(final AccountId accountId) throws OnlineServiceException;

    /**
     * Return AccountId to lastLogin time mapping, for provided {@link AccountId} collection.
     *
     * @param accounts {@link AccountId} collection
     * @return {@link AccountId} - last login time mapping
     * @throws OnlineServiceException on errors
     */
    Map<AccountId, Long> readLastLoginTime(final List<AccountId> accounts) throws OnlineServiceException;

    /**
     * Return AccountId to last activity time mapping, for provided {@link AccountId} collection.
     *
     * @param accounts {@link AccountId} collection
     * @return {@link AccountId} - last activity time mapping
     * @throws OnlineServiceException on errors
     */
    Map<AccountId, Long> readLastActivityTime(final List<AccountId> accounts) throws OnlineServiceException;

    /**
     * Remove all stored data about incoming {@link AccountId}, performs log-out if still online.
     * Useful to use  current after  performing Account#remove.
     *
     * @param accountId {@link AccountId} target account
     * @throws OnlineServiceException on errors
     */
    void removeActivityData(final AccountId accountId) throws OnlineServiceException;

}
