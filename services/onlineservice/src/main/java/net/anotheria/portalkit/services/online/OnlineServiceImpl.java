package net.anotheria.portalkit.services.online;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.persistence.ActivityNotFoundInPersistenceServiceException;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceServiceException;
import net.anotheria.portalkit.services.online.storage.OnlineStorage;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import net.anotheria.util.log.LogMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@link OnlineService} implementation.
 *
 * @author h3llka
 */
@Monitor(subsystem = "online", category = "portalkit-service")
public class OnlineServiceImpl implements OnlineService {
	/**
	 * Logging util instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(OnlineServiceImpl.class);
	/**
	 * {@link ActivityPersistenceService} instance, provides basic crud operations for UserActivity stuff..
	 */
	private ActivityPersistenceService persistence;
	/**
	 * {@link OnlineStorage} instance.
	 */
	private OnlineStorage onlineUserStorage;
	/**
	 * {@link IdBasedLockManager} instance.
	 */
	private IdBasedLockManager<AccountId> lockManager;
	/**
	 * {@link OnlineServiceConfiguration} config instance.
	 */
	private OnlineServiceConfiguration config;


	/**
	 * Constructor.
	 */
	protected OnlineServiceImpl() {
		try {
			persistence = MetaFactory.get(ActivityPersistenceService.class);
		} catch (MetaFactoryException e) {
			LOG.error("ActivityPersistenceService init failed", e);
			throw new RuntimeException("ActivityPersistenceService init failed", e);
		}
		onlineUserStorage = new OnlineStorage();
		lockManager = new SafeIdBasedLockManager<AccountId>();
		config = OnlineServiceConfiguration.getInstance();
		initCleanUpTimer();

	}

	/**
	 * Initialise clean up timer task.
	 */
	private void initCleanUpTimer() {
		//init cleanUp timer!!
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					cleanUpInactiveAccounts();
				} catch (Exception e) {
					LOG.error("Uncaught exception in cleanUpInactiveAccounts() timer task", e);
				}
			}

		}, 0L, config.getInactiveAccountsCleanUpInterval());
	}

	@Override
	public void notifyLoggedIn(final AccountId account) throws OnlineServiceException {
		if (account == null)
			throw new IllegalArgumentException("Incoming parameter : [account] in not valid");

		IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
		lock.lock();

		try {
			if (onlineUserStorage.isAccountOnline(account))
				throw new AccountIsOnlineException(account);

			final long lastLoginNanoTime = getNanoTime();
			persistence.saveLastLogin(account, toMillis(lastLoginNanoTime));
			onlineUserStorage.notifyLoggedIn(account, lastLoginNanoTime);
		} catch (ActivityPersistenceServiceException e) {
			final String message = LogMessageUtil.failMsg(e, account);
			LOG.error(message, e);
			throw new OnlineServiceException(message, e);
		} finally {
			lock.unlock();
		}
	}


	@Override
	public void notifyUserActivity(final AccountId account) throws OnlineServiceException {
		if (account == null)
			throw new IllegalArgumentException("Incoming parameter : [account] in not valid");

		IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
		lock.lock();

		try {
			if (!onlineUserStorage.isAccountOnline(account))
				throw new AccountIsOfflineException(account);

			final long lastActivityTime = getNanoTime();
			persistence.saveLastActivity(account, toMillis(lastActivityTime));
			onlineUserStorage.notifyActivity(account, lastActivityTime);
		} catch (ActivityPersistenceServiceException e) {
			final String message = LogMessageUtil.failMsg(e, account);
			LOG.error(message, e);
			throw new OnlineServiceException(message, e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void notifyLoggedOut(final AccountId account) throws OnlineServiceException {
		if (account == null)
			throw new IllegalArgumentException("Incoming parameter : [account] in not valid");

		IdBasedLock<AccountId> lock = lockManager.obtainLock(account);
		lock.lock();

		try {
			if (!onlineUserStorage.isAccountOnline(account))
				throw new AccountIsOfflineException(account);

			onlineUserStorage.notifyLogOut(account);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isOnline(final AccountId account) throws OnlineServiceException {
		if (account == null)
			throw new IllegalArgumentException("Incoming parameter : [account] in not valid");

		return onlineUserStorage.isAccountOnline(account);
	}

	@Override
	public List<AccountId> readOnlineUsers(OnlineAccountReadCriteria criteria) throws OnlineServiceException {

		if (criteria == null)
			throw new IllegalArgumentException("Incoming parameter : [criteria] in not valid");

		return onlineUserStorage.readOnlineUsers(criteria);
	}

	@Override
	public long readLastLogin(final AccountId account) throws OnlineServiceException {
		return readTime(account, AccountTimingPropertyName.LAST_LOGIN_TIME);

	}

	@Override
	public long readLastActivity(final AccountId account) throws OnlineServiceException {
		return readTime(account, AccountTimingPropertyName.LAST_ACTIVITY_TIME);
	}

	@Override
	public Map<AccountId, Long> readLastLoginTime(final List<AccountId> accounts) throws OnlineServiceException {
		if (accounts == null)
			throw new IllegalArgumentException("Incoming parameter : [accounts] collection in not valid");

		if (accounts.isEmpty())
			return new HashMap<AccountId, Long>();

		return readTime(accounts, AccountTimingPropertyName.LAST_LOGIN_TIME);

	}

	@Override
	public Map<AccountId, Long> readLastActivityTime(final List<AccountId> accounts) throws OnlineServiceException {
		if (accounts == null)
			throw new IllegalArgumentException("Incoming parameter : [accounts] collection in not valid");

		if (accounts.isEmpty())
			return new HashMap<AccountId, Long>();

		return readTime(accounts, AccountTimingPropertyName.LAST_ACTIVITY_TIME);

	}

	@Override
	public void removeActivityData(AccountId accountId) throws OnlineServiceException {
		if (accountId == null)
			throw new IllegalArgumentException("Incoming parameter : [accountId] in not valid");

		IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
		lock.lock();

		try {
			persistence.deleteActivityEntry(accountId);
			//removing from Online storage
			if (onlineUserStorage.isAccountOnline(accountId))
				onlineUserStorage.notifyLogOut(accountId);
		} catch (ActivityPersistenceServiceException e) {
			final String message = LogMessageUtil.failMsg(e, accountId);
			LOG.error(message, e);
			throw new OnlineServiceException(message, e);
		} finally {
			lock.unlock();
		}
	}


	/**
	 * Read single time, from online storage - or persistence.
	 *
	 * @param account  {@link AccountId}
	 * @param property {@link AccountTimingPropertyName} timeStamp type which should be read
	 * @return time which was queried
	 * @throws OnlineServiceException on errors from persistence service
	 */
	private long readTime(final AccountId account, final AccountTimingPropertyName property) throws OnlineServiceException {
		if (account == null)
			throw new IllegalArgumentException("Incoming parameter : [account] in not valid");
		if (property == null)
			throw new IllegalArgumentException("Incoming parameter : [property] is not valid");

		try {
			final boolean isOnline = onlineUserStorage.isAccountOnline(account);
			switch (property) {
				case LAST_ACTIVITY_TIME:
					try {
						return isOnline ? onlineUserStorage.getLastActivityTimeStamp(account) : persistence.readLastActivity(account);
					} catch (AccountIsOfflineException aiof) {
						//uups - went offline! Let's get from storage
						return persistence.readLastActivity(account);
					}

				case LAST_LOGIN_TIME:
					try {
						return isOnline ? onlineUserStorage.getLastLoginTimeStamp(account) : persistence.readLastLogin(account);
					} catch (AccountIsOfflineException aiof) {
						//uups - went offline! Let's get from storage
						return persistence.readLastLogin(account);
					}

				default:
					throw new AssertionError(property + " is no supported");
			}

		} catch (ActivityNotFoundInPersistenceServiceException e) {
			throw new NoActivityDataFoundException(account, property.name());
		} catch (ActivityPersistenceServiceException e) {
			final String msg = LogMessageUtil.failMsg(e, account, property);
			LOG.error(msg, e);
			throw new OnlineServiceException(msg, e);

		}
	}


	/**
	 * Read defined time collection, from online storage - or persistence.
	 *
	 * @param accounts {@link AccountId} collection to read
	 * @param property {@link AccountTimingPropertyName} timeStamp type which should be read
	 * @return time which was queried
	 * @throws OnlineServiceException on errors from persistence service
	 */
	private Map<AccountId, Long> readTime(final List<AccountId> accounts, final AccountTimingPropertyName property) throws OnlineServiceException {
		if (accounts == null)
			throw new IllegalArgumentException("Incoming parameter : [accounts] collection in not valid");
		if (property == null)
			throw new IllegalArgumentException("Incoming parameter : [property] is not valid");

		Map<AccountId, Long> result = new HashMap<AccountId, Long>();
		if (accounts.isEmpty())
			return result;


		final List<AccountId> accountIdsCopy = new ArrayList<AccountId>(accounts);
		try {
			switch (property) {
				case LAST_ACTIVITY_TIME:
					//fetching online
					result.putAll(onlineUserStorage.getLastActivityTimeStamps(accountIdsCopy));

					if (!result.isEmpty())
						accountIdsCopy.removeAll(new ArrayList<AccountId>(result.keySet()));
					if (accountIdsCopy.isEmpty())
						return result;
					//adding results from service
					result.putAll(persistence.readLastActivity(accountIdsCopy));
					return result;


				case LAST_LOGIN_TIME:
					//fetching online
					result.putAll(onlineUserStorage.getLastLoginTimeStamps(accountIdsCopy));
					if (!result.isEmpty())
						accountIdsCopy.removeAll(new ArrayList<AccountId>(result.keySet()));
					if (accountIdsCopy.isEmpty())
						return result;
					//adding results from service
					result.putAll(persistence.readLastLogin(accountIdsCopy));
					return result;


				default:
					throw new AssertionError(property + " is no supported");
			}

		} catch (ActivityPersistenceServiceException e) {
			final String msg = LogMessageUtil.failMsg(e, accounts.size(), property);
			LOG.error(msg, e);
			throw new OnlineServiceException(msg, e);
		}


	}

	/**
	 * Call {@link OnlineStorage#cleanUpInactiveAccounts()}, to clean inactive/expired accounts from online storage.
	 */
	private void cleanUpInactiveAccounts() {
		onlineUserStorage.cleanUpInactiveAccounts();
	}

	/**
	 * Convert nano time to time in millis.
	 *
	 * @param nanoTime incoming source time in nano seconds
	 * @return transformed millis time
	 */
	private static long toMillis(final long nanoTime) {
		return TimeUnit.MILLISECONDS.transformNanos(nanoTime);
	}

	/**
	 * Return current system time in nano-seconds.
	 *
	 * @return nano time
	 */
	private static long getNanoTime() {
		// Amount of nano-seconds in 1 milli second
		return System.currentTimeMillis() * 1000000;
	}

	/**
	 * Contains constants for last-login, activity time read.
	 */
	private enum AccountTimingPropertyName {
		/**
		 * Read last login.
		 */
		LAST_LOGIN_TIME,
		/**
		 * Read last activity.
		 */
		LAST_ACTIVITY_TIME
	}


}
