package net.anotheria.portalkit.apis.online;

import net.anotheria.util.TimeUnit;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for {@link OnlineAPI}.
 * 
 * @author h3llka
 */
@ConfigureMe(name = "pk-online-api-configuration")
public final class OnlineAPIConfiguration {
	/**
	 * Instance property.
	 */
	private static volatile OnlineAPIConfiguration instance;
	/**
	 * Monitor instance...
	 */
	private static final Object lock = new Object();
	/**
	 * Allow to configure interval of activity notifications delegation. Next activity notification will be forward to server only after interval end.
	 */
	@Configure
	private long activityUpdateInterval;
	/**
	 * Allow to notify Login via {@link net.anotheria.portalkit.services.online.OnlineService#notifyLoggedIn(net.anotheria.portalkit.services.common.AccountId)}
	 * - in case if {@link net.anotheria.portalkit.services.online.OnlineService#notifyUserActivity(net.anotheria.portalkit.services.common.AccountId)} fails
	 * with {@link net.anotheria.portalkit.services.online.AccountIsOfflineException}.
	 */
	@Configure
	private boolean performLoginNotificationOnActivityOfflineErrors;

	/**
	 * Allow to notify Activity via
	 * {@link net.anotheria.portalkit.services.online.OnlineService#notifyUserActivity(net.anotheria.portalkit.services.common.AccountId)} - in case if
	 * {@link net.anotheria.portalkit.services.online.OnlineService#notifyLoggedIn(net.anotheria.portalkit.services.common.AccountId)} fails with
	 * {@link net.anotheria.portalkit.services.online.AccountIsOnlineException}.
	 */
	@Configure
	private boolean performActivityUpdateOnLoginOnlineErrors;

	/**
	 * Allow to select sync/async communication with {@link net.anotheria.portalkit.services.online.OnlineService} notify - interface methods.
	 */
	@Configure
	private boolean performLoginNotificationAsync;

	/**
	 * Allow to select sync/async communication with {@link net.anotheria.portalkit.services.online.OnlineService} notify - interface methods.
	 */
	@Configure
	private boolean performLogoutNotificationAsync;

	/**
	 * Allow to select sync/async communication with {@link net.anotheria.portalkit.services.online.OnlineService} notify - interface methods.
	 */
	@Configure
	private boolean performActivityUpdateNotificationAsync;
	/**
	 * Sleep time for async processor queue. Handling activity events async...
	 */
	@Configure
	private long activityEventProcessorSleepTime;
	/**
	 * Size of async processor queue. Handling activity events async...
	 */
	@Configure
	private int activityEventProcessorSize;
	/**
	 * Amount of channels for activity events async queue.
	 */
	@Configure
	private int activityEventProcessorChannelsAmount;

	/**
	 * Return {@link OnlineAPIConfiguration} instance.
	 * 
	 * @return {@link OnlineAPIConfiguration} configured
	 */
	public static OnlineAPIConfiguration getInstance() {
		if (instance != null)
			return instance;

		synchronized (lock) {
			if (instance != null)
				return instance;
			instance = new OnlineAPIConfiguration();
			try {
				ConfigurationManager.INSTANCE.configure(instance);
			} catch (Exception e) {
				Logger.getLogger(OnlineAPIConfiguration.class).warn("Configuration failed, relying on defaults. " + e.getMessage());
			}
			return instance;
		}
	}

	/**
	 * Constructor.
	 */
	private OnlineAPIConfiguration() {
		this.activityUpdateInterval = TimeUnit.MINUTE.getMillis() * 5;
		this.performLoginNotificationOnActivityOfflineErrors = true;
		this.performLoginNotificationAsync = false;
		this.performLogoutNotificationAsync = false;
		this.performActivityUpdateNotificationAsync = false;
		this.performActivityUpdateOnLoginOnlineErrors = true;
		this.activityEventProcessorSleepTime = 200L;
		this.activityEventProcessorSize = 1000;
		this.activityEventProcessorChannelsAmount = 10;
	}

	public long getActivityUpdateInterval() {
		return activityUpdateInterval;
	}

	public void setActivityUpdateInterval(long activityUpdateInterval) {
		this.activityUpdateInterval = activityUpdateInterval;
	}

	public boolean isPerformLoginNotificationOnActivityOfflineErrors() {
		return performLoginNotificationOnActivityOfflineErrors;
	}

	public void setPerformLoginNotificationOnActivityOfflineErrors(boolean performLoginNotificationOnActivityOfflineErrors) {
		this.performLoginNotificationOnActivityOfflineErrors = performLoginNotificationOnActivityOfflineErrors;
	}

	public boolean isPerformActivityUpdateOnLoginOnlineErrors() {
		return performActivityUpdateOnLoginOnlineErrors;
	}

	public void setPerformActivityUpdateOnLoginOnlineErrors(boolean performActivityUpdateOnLoginOnlineErrors) {
		this.performActivityUpdateOnLoginOnlineErrors = performActivityUpdateOnLoginOnlineErrors;
	}

	public long getActivityEventProcessorSleepTime() {
		return activityEventProcessorSleepTime;
	}

	public void setActivityEventProcessorSleepTime(long activityEventProcessorSleepTime) {
		this.activityEventProcessorSleepTime = activityEventProcessorSleepTime;
	}

	public int getActivityEventProcessorSize() {
		return activityEventProcessorSize;
	}

	public void setActivityEventProcessorSize(int activityEventProcessorSize) {
		this.activityEventProcessorSize = activityEventProcessorSize;
	}

	public int getActivityEventProcessorChannelsAmount() {
		return activityEventProcessorChannelsAmount;
	}

	public void setActivityEventProcessorChannelsAmount(int activityEventProcessorChannelsAmount) {
		this.activityEventProcessorChannelsAmount = activityEventProcessorChannelsAmount;
	}

	public boolean isPerformLoginNotificationAsync() {
		return performLoginNotificationAsync;
	}

	public void setPerformLoginNotificationAsync(boolean performLoginNotificationAsync) {
		this.performLoginNotificationAsync = performLoginNotificationAsync;
	}

	public boolean isPerformLogoutNotificationAsync() {
		return performLogoutNotificationAsync;
	}

	public void setPerformLogoutNotificationAsync(boolean performLogoutNotificationAsync) {
		this.performLogoutNotificationAsync = performLogoutNotificationAsync;
	}

	public boolean isPerformActivityUpdateNotificationAsync() {
		return performActivityUpdateNotificationAsync;
	}

	public void setPerformActivityUpdateNotificationAsync(boolean performActivityUpdateNotificationAsync) {
		this.performActivityUpdateNotificationAsync = performActivityUpdateNotificationAsync;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
		builder.append(" [activityUpdateInterval=").append(activityUpdateInterval);
		builder.append(", performLoginNotificationOnActivityOfflineErrors=").append(performLoginNotificationOnActivityOfflineErrors);
		builder.append(", performActivityUpdateOnLoginOnlineErrors=").append(performActivityUpdateOnLoginOnlineErrors);
		builder.append(", performLoginNotificationAsync=").append(performLoginNotificationAsync);
		builder.append(", performLogoutNotificationAsync=").append(performLogoutNotificationAsync);
		builder.append(", performActivityUpdateNotificationAsync=").append(performActivityUpdateNotificationAsync);
		builder.append(", activityEventProcessorSleepTime=").append(activityEventProcessorSleepTime);
		builder.append(", activityEventProcessorSize=").append(activityEventProcessorSize);
		builder.append(", activityEventProcessorChannelsAmount=").append(activityEventProcessorChannelsAmount);
		builder.append("]");
		return builder.toString();
	}

}
