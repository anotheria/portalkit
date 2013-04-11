package net.anotheria.portalkit.services.online.storage;

import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.AccountIsOfflineException;
import net.anotheria.portalkit.services.online.AccountIsOnlineException;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.SortProperty;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.TimeBasedQueryDirection;
import net.anotheria.portalkit.services.online.OnlineServiceConfiguration;
import net.anotheria.portalkit.services.online.events.ActivityAnnouncer;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Storage for currently logged in users.
 *
 * @author h3llka
 */
public class OnlineStorage {

    /**
     * {@link Logger} instance.
     */
    private static final Logger LOG = Logger.getLogger(OnlineStorage.class);
    /**
     * Amount of nano-seconds in 1 milli second.
     */
    private static final long NANO_SECONDS_IN_ONE_MILLI_SECOND = 1000000L;
    /**
     * Local storage for {@link OnlineUserData}.
     */
    private ConcurrentMap<AccountId, OnlineUserData> onlineUsers;
    /**
     * Internal storage which maps lastLogin time to {@link AccountId}.
     */
    private ConcurrentNavigableMap<Long, AccountId> lastLoginIndex;
    /**
     * Internal storage which maps lastActivity time to {@link AccountId}.
     */
    private ConcurrentNavigableMap<Long, AccountId> lastActivityIndex;
    /**
     * {@link IdBasedLockManager} instance.
     */
    private IdBasedLockManager<AccountId> lockManager;
    /**
     * {@link OnlineServiceConfiguration} instance.
     */
    private transient OnlineServiceConfiguration config;
    /**
     * {@link ActivityAnnouncer} instance.
     */
    private ActivityAnnouncer announcer;


    /**
     * Constructor.
     */
    public OnlineStorage() {
        onlineUsers = new ConcurrentHashMap<AccountId, OnlineUserData>();
        lastLoginIndex = new ConcurrentSkipListMap<Long, AccountId>();
        lastActivityIndex = new ConcurrentSkipListMap<Long, AccountId>();
        lockManager = new SafeIdBasedLockManager<AccountId>();
        config = OnlineServiceConfiguration.getInstance();
        announcer = new ActivityAnnouncer();
    }


    /**
     * Return {@code true} if and only if {@link AccountId} which was provided can be found inside internal storage.
     *
     * @param account {@link AccountId}
     * @return boolean value
     */
    public boolean isAccountOnline(final AccountId account) {
        if (account == null)
            throw new IllegalArgumentException("not valid account passed");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            return isOnline(account);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Add current {@link AccountId} to the storage.
     * NOTE : last login parameter is passed as Nano-time timestamp.
     *
     * @param account           {@link AccountId} to store
     * @param lastLoginNanoTime last login time in nano's
     * @throws AccountIsOnlineException in case when user can be found in internal storage
     */
    public void notifyLoggedIn(final AccountId account, final long lastLoginNanoTime) throws AccountIsOnlineException {
        if (account == null)
            throw new IllegalArgumentException("not valid account passed");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            if (isOnline(account))
                throw new AccountIsOnlineException(account);

            //trying to generate unique last activity time in nano seconds...
            long lastActivity = indexLastActivity(account, lastLoginNanoTime);
            //trying to generate unique last login time in nano seconds...
            long lastLogin = indexLastLogin(account, lastLoginNanoTime);
            //put to storage
            onlineUsers.put(account, new OnlineUserData(account, lastLogin, lastActivity));

            //event announce
            announcer.accountLoggedIn(account, TimeUnit.MILLISECONDS.transformNanos(lastLoginNanoTime));
        } finally {
            lock.unlock();
        }

    }

    /**
     * Update activity time  for {@link AccountId}, which is online (can be found in the storage).
     * NOTE : last activity parameter is passed as Nano-time timestamp.
     *
     * @param account              {@link AccountId} to store
     * @param lastActivityNanoTime last activity time in nano's
     * @throws AccountIsOfflineException in case when user can not be found in internal storage
     */
    public void notifyActivity(AccountId account, long lastActivityNanoTime) throws AccountIsOfflineException {
        if (account == null)
            throw new IllegalArgumentException("not valid account passed");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            if (!isOnline(account))
                throw new AccountIsOfflineException(account);

            //put to storage
            OnlineUserData data = onlineUsers.get(account);
            //remove last activity index
            lastActivityIndex.remove(data.getLastActivityNanoTime());
            //update last activity index
            //generate unique last activity
            long lastActivity = indexLastActivity(account, lastActivityNanoTime);
            data.setLastActivityNanoTime(lastActivity);

            //event announce
            announcer.accountActivityChange(account, TimeUnit.MILLISECONDS.transformNanos(lastActivityNanoTime));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove {@link AccountId} from internal storage and indexes.
     *
     * @param account {@link AccountId}
     * @throws AccountIsOfflineException in case when user can't be found among online ( inside storage).
     */
    public void notifyLogOut(final AccountId account) throws AccountIsOfflineException {
        notifyLogOut(account, true);
    }

    /**
     * {@inheritDoc #notifyLogOut(AccountId)}.
     * Allow to select - is async event should be send, or not.
     *
     * @param account   {@link AccountId}
     * @param sendEvent {@code true} if event should be send, false otherwise.
     * @throws AccountIsOfflineException in case of error
     */
    private void notifyLogOut(final AccountId account, boolean sendEvent) throws AccountIsOfflineException {
        if (account == null)
            throw new IllegalArgumentException("not valid account passed");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            if (!isOnline(account))
                throw new AccountIsOfflineException(account);

            //put to storage
            OnlineUserData data = onlineUsers.get(account);
            //remove last activity idx
            lastActivityIndex.remove(data.getLastActivityNanoTime());
            //remove last login idx
            lastLoginIndex.remove(data.getLastLoginNanoTime());
            //remove data
            onlineUsers.remove(account);

            //event announce
            if (sendEvent)
                announcer.accountLoggedOut(account);

        } finally {
            lock.unlock();
        }
    }

    /**
     * Return last activity time for selected {@link AccountId}.
     * Note - method return time in millis.
     *
     * @param account {@link AccountId}
     * @return last activity time
     * @throws AccountIsOfflineException in case if user went offline
     */
    public long getLastActivityTimeStamp(final AccountId account) throws AccountIsOfflineException {
        if (account == null)
            throw new IllegalArgumentException("not valid account passed");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            if (!isOnline(account))
                throw new AccountIsOfflineException(account);
            //put to storage
            OnlineUserData data = onlineUsers.get(account);
            return TimeUnit.MILLISECONDS.transformNanos(data.getLastActivityNanoTime());

        } finally {
            lock.unlock();
        }
    }


    /**
     * Return last login time for selected {@link AccountId}.
     * Note - method return time in millis.
     *
     * @param account {@link AccountId}
     * @return last activity time
     * @throws AccountIsOfflineException in case if user went offline
     */
    public long getLastLoginTimeStamp(final AccountId account) throws AccountIsOfflineException {
        if (account == null)
            throw new IllegalArgumentException("not valid account passed");

        IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
        lock.lock();
        try {
            if (!isOnline(account))
                throw new AccountIsOfflineException(account);
            //put to storage
            OnlineUserData data = onlineUsers.get(account);
            return TimeUnit.MILLISECONDS.transformNanos(data.getLastLoginNanoTime());

        } finally {
            lock.unlock();
        }
    }

    /**
     * Return AccountId to last activity time mapping!
     * NOTE - if some user is not online - data won't present in the result.
     *
     * @param accounts {@link AccountId} collection
     * @return collection with  AccountId to last activity mapping
     */
    public Map<AccountId, Long> getLastActivityTimeStamps(List<AccountId> accounts) {
        if (accounts == null)
            throw new IllegalArgumentException("not valid accounts passed");

        Map<AccountId, Long> result = new HashMap<AccountId, Long>();
        for (AccountId acc : accounts) {
            try {
                long time = getLastActivityTimeStamp(acc);
                result.put(acc, time);
            } catch (AccountIsOfflineException e) {
                if (LOG.isTraceEnabled())
                    LOG.trace("Expected exception - " + e.getMessage(), e);
            }
        }

        return result;
    }

    /**
     * Return AccountId to last login time mapping!
     * NOTE - if some user is not online - data won't present in the result.
     *
     * @param accounts {@link AccountId} collection
     * @return collection with  AccountId to last login mapping
     */
    public Map<AccountId, Long> getLastLoginTimeStamps(List<AccountId> accounts) {
        if (accounts == null)
            throw new IllegalArgumentException("not valid accounts passed");

        Map<AccountId, Long> result = new HashMap<AccountId, Long>();
        for (AccountId acc : accounts) {
            try {
                long time = getLastLoginTimeStamp(acc);
                result.put(acc, time);
            } catch (AccountIsOfflineException e) {
                if (LOG.isTraceEnabled())
                    LOG.trace("Expected exception - " + e.getMessage(), e);
            }
        }

        return result;
    }

    /**
     * Return {@link AccountId} collection. Collection read properties specified in context of {@link OnlineAccountReadCriteria}.
     *
     * @param criteria {@link OnlineAccountReadCriteria} filtering and sorting rules for data fetch
     * @return {@link AccountId} collection
     */
    public List<AccountId> readOnlineUsers(OnlineAccountReadCriteria criteria) {
        if (criteria == null)
            throw new IllegalArgumentException("not valid criteria passed");

        if (LOG.isDebugEnabled())
            LOG.debug("query : " + criteria);

        OnlineAccountReadCriteria.SortProperty sortBy = criteria.getProperty();
        //read by Acc ids...
        if (criteria.getAccounts() != null && !criteria.getAccounts().isEmpty()) {

            ArrayList<AccountId> allOnlineAccountsList;
            switch (sortBy) {
                case NONE:
                    allOnlineAccountsList = new ArrayList<AccountId>(onlineUsers.keySet());
                    break;
                case LAST_LOGIN:
                    allOnlineAccountsList = criteria.isAscDirection() ? new ArrayList<AccountId>(lastLoginIndex.values()) : new ArrayList<AccountId>(lastLoginIndex.descendingMap().values());
                    break;
                case LAST_ACTIVITY:
                    allOnlineAccountsList = criteria.isAscDirection() ? new ArrayList<AccountId>(lastActivityIndex.values()) : new ArrayList<AccountId>(lastActivityIndex.descendingMap().values());
                    break;
                default:
                    throw new AssertionError(sortBy + " as sort direction is not supported");
            }

            allOnlineAccountsList.retainAll(criteria.getAccounts());
            return new ArrayList<AccountId>(allOnlineAccountsList);

        }

        // Time Querying part
        switch (sortBy) {
            case NONE:
                List<AccountId> onlineAccounts = new ArrayList<AccountId>(onlineUsers.keySet());
                if (criteria.getTimeDirection() != TimeBasedQueryDirection.NONE)
                    LOG.warn("current " + criteria.getTimeDirection() + " can't be applied to sortBy[" + sortBy + "], relying on defaults");
                return cutList(onlineAccounts, criteria.getLimit());

            case LAST_ACTIVITY:
            case LAST_LOGIN:
                NavigableMap<Long, AccountId> map = sortBy == SortProperty.LAST_ACTIVITY ? lastActivityIndex : lastLoginIndex;
                TimeBasedQueryDirection timingDirection = criteria.getTimeDirection();
                switch (timingDirection) {
                    case NONE:
                        // only for debugging purposes  )
                        if (!criteria.isAscDirection())
                            map = map.descendingMap();
                        return cutList(new ArrayList<AccountId>(map.values()), criteria.getLimit());
                    case BETWEEN:
                        //between !  interval start/end exclusive ...  it's not required to check for desc direction! here
                        return cutList(new ArrayList<AccountId>(map.subMap(criteria.getFromTime(), false, criteria.getToTime(), false).values()), criteria.getLimit());
                    case AFTER:
                        //all elements which are greater then selected time stamp .. selected time  exclusive... it's not required to check for desc direction! here
                        return cutList(new ArrayList<AccountId>(map.tailMap(criteria.getTimeStamp(), false).values()), criteria.getLimit());
                    case BEFORE:
                        //all elements which are less then selected time stamp .. selected time  exclusive...  it's not required to check for desc direction! here
                        return cutList(new ArrayList<AccountId>(map.headMap(criteria.getTimeStamp(), false).values()), criteria.getLimit());
                    default:
                        throw new AssertionError(timingDirection + " as time-based-query direction is  not supported");
                }

            default:
                throw new AssertionError(sortBy + " as sort direction is not supported");
        }


    }

    /**
     * Return amount of currently logged in users.
     *
     * @return online users amount
     */
    public int getOnlineUsersAmount() {
        return onlineUsers.size();
    }

    /**
     * Return amount of items inside last login idx collection.
     *
     * @return last login idx size
     */
    protected int getLastLoginIdxSize() {
        return lastLoginIndex.size();
    }

    /**
     * Return amount of items inside last activity idx collection.
     *
     * @return last activity idx size
     */
    protected int getLastActivityIdxSize() {
        return lastActivityIndex.size();
    }

    /**
     * Perform inactive accounts logOut.
     * Each account which inactivity interval passed - will be logged out.
     * Inactivity interval configured via {@link OnlineServiceConfiguration#getMaxAccountInactivityInterval()}  property.
     */
    public void cleanUpInactiveAccounts() {
        final boolean isInfoLoggingEnabled = LOG.isInfoEnabled();
        //transforming to the nano's
        final long nanoTimeExpirationInterval = config.getMaxAccountInactivityInterval() * NANO_SECONDS_IN_ONE_MILLI_SECOND;
        List<AccountId> toCleanUp = getExpiredOnlineAccounts(nanoTimeExpirationInterval);
        if (isInfoLoggingEnabled)
            LOG.info("There are " + toCleanUp.size() + " of inactive accounts - which should be cleaned UP. InactivityInterval [" + TimeUnit.SECONDS.transformNanos(nanoTimeExpirationInterval) + "]sec.");
        int successCounter = 0;
        if (toCleanUp.isEmpty())
            return;
        for (AccountId acc : toCleanUp) {
            try {
                notifyLogOut(acc, false);
                successCounter++;
            } catch (AccountIsOfflineException e) {
                if (LOG.isDebugEnabled())
                    LOG.debug("CleanUp failed for [" + acc + "], cause it's offline!", e);
            }
        }
        //cleanUp announce!
        announcer.cleanUp(toCleanUp);

        if (isInfoLoggingEnabled)
            LOG.info(successCounter + " inactive/expired accounts were cleaned up");

    }

    /**
     * Return {@link AccountId} collection, which contains all online accounts with last activity time - less than '{@link System#nanoTime()} - expirationPeriod'.
     * This are actually accounts which should be automatically logged off due to inactivity...
     *
     * @param expirationPeriod period in nano-seconds
     * @return {@link AccountId} which should be cleaned up
     */
    private List<AccountId> getExpiredOnlineAccounts(final long expirationPeriod) {
        final long time = System.nanoTime();
        if (expirationPeriod > time)
            throw new IllegalArgumentException("Provided period is not valid! It should be less than current nano-time");
        final long expirationTime = time - expirationPeriod;
        ConcurrentNavigableMap<Long, AccountId> expiredAccounts = lastActivityIndex.headMap(expirationTime, true);

        return expiredAccounts.isEmpty() ? Collections.<AccountId>emptyList() : new ArrayList<AccountId>(expiredAccounts.values());

    }


    /**
     * Indexing last login time. Possible case that there will be last login with nano-time as passed, so method will try to generate new one, by incrementing
     * incoming value, till unique will be found.
     *
     * @param acc      {@link AccountId}
     * @param nanoTime time to index
     * @return unique time with which indexing was performed
     */
    private long indexLastLogin(final AccountId acc, final long nanoTime) {
        long time = nanoTime;
        do {
            if (lastLoginIndex.putIfAbsent(time, acc) == null)
                break;
            time++;
        } while (true);
        return time;
    }

    /**
     * Indexing last activity time. Possible case that there will be last activity with nano-time as passed, so method will try to generate new one, by incrementing
     * incoming value, till unique will be found.
     *
     * @param acc      {@link AccountId}
     * @param nanoTime time to index
     * @return unique time with which indexing was performed
     */
    private long indexLastActivity(final AccountId acc, final long nanoTime) {
        long time = nanoTime;
        do {
            if (lastActivityIndex.putIfAbsent(time, acc) == null)
                break;
            time++;
        } while (true);
        return time;
    }


    /**
     * Internal non thread-safe method, which return {@code true} in case when user-data can be found inside storage.
     *
     * @param account {@link AccountId}
     * @return boolean value
     */
    private boolean isOnline(final AccountId account) {
        return onlineUsers.containsKey(account) && onlineUsers.get(account) != null;
    }

    /**
     * Cur not required data from source list. Simply creating sub-list in case when source contains more elements than expected via size param.
     *
     * @param source Collection with data
     * @param size   data max size which is required
     * @param <T>    - type parameter
     * @return {@link List}
     */
    private static <T> List<T> cutList(final List<T> source, final int size) {
        if (source == null)
            throw new IllegalArgumentException("source parameter is not valid");
        if (size <= 0)
            throw new IllegalArgumentException("size is not valid [" + size + "]. Positive value greater than 0 is expected");

        return source.size() <= size ? source : new ArrayList<T>(source.subList(0, size));

    }

    /**
     * Playing with ev1l :P.
     *
     * @param args -...
     */
    public static void main(String[] args) {
        ConcurrentNavigableMap<Long, AccountId> test = new ConcurrentSkipListMap<Long, AccountId>();

        test.put(1L, new AccountId(1 + ""));
        test.put(2L, new AccountId(2 + ""));
        test.put(3L, new AccountId(3 + ""));
        test.put(4L, new AccountId(4 + ""));
        test.put(5L, new AccountId(5 + ""));

        ConcurrentNavigableMap<Long, AccountId> test2 = new ConcurrentSkipListMap<Long, AccountId>(test.descendingMap());


        List<AccountId> accountIds = new ArrayList<AccountId>();

        accountIds.add(new AccountId(1 + ""));
        accountIds.add(new AccountId(3 + ""));
        accountIds.add(new AccountId(5 + ""));


        List<AccountId> asc = new ArrayList<AccountId>(test.values());
        List<AccountId> desc = new ArrayList<AccountId>(test2.values());

        System.out.println("ASC" + asc);
        System.out.println("DESC" + desc);

        asc.retainAll(accountIds);
        desc.retainAll(accountIds);
        System.out.println("ASC - filtered" + asc);
        System.out.println("DESC - filtered" + desc);


        ConcurrentNavigableMap<String, AccountId> test3 = new ConcurrentSkipListMap<String, AccountId>();
        test3.put(new AccountId(1 + "").getInternalId(), new AccountId(1 + ""));
        test3.put(new AccountId(2 + "").getInternalId(), new AccountId(2 + ""));
        test3.put(new AccountId(3 + "").getInternalId(), new AccountId(3 + ""));

        System.out.println(test3);
    }


}
