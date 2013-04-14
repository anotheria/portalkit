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
     * Allow to configure interval of activity notifications delegation.
     * Next activity notification will be forward to server only after interval end.
     */
    @Configure
    private long activityUpdateInterval;
    /**
     * Allow to notify Login via {@link net.anotheria.portalkit.services.online.OnlineService#notifyLoggedIn(net.anotheria.portalkit.services.common.AccountId)} - in case
     * if {@link net.anotheria.portalkit.services.online.OnlineService#notifyUserActivity(net.anotheria.portalkit.services.common.AccountId)} fails with
     * {@link net.anotheria.portalkit.services.online.AccountIsOfflineException}.
     */
    @Configure
    private boolean performLoginNotificationOnActivityOfflineErrors;

    /**
     * Allow to notify Activity via {@link net.anotheria.portalkit.services.online.OnlineService#notifyUserActivity(net.anotheria.portalkit.services.common.AccountId)} - in case
     * if {@link net.anotheria.portalkit.services.online.OnlineService#notifyLoggedIn(net.anotheria.portalkit.services.common.AccountId)} fails with
     * {@link net.anotheria.portalkit.services.online.AccountIsOnlineException}.
     */
    @Configure
    private boolean performActivityUpdateOnLoginOnlineErrors;

    /**
     * Allow to select sync/async communication with  {@link net.anotheria.portalkit.services.online.OnlineService}
     * notify - interface methods.
     */
    @Configure
    private boolean performNotificationAsync;
    /**
     * Sleep time for async processor queue.
     * Handling activity events async...
     */
    @Configure
    private long activityEventProcessorSleepTime;
    /**
     * Size of async processor queue.
     * Handling activity events async...
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
        this.performNotificationAsync = true;
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

    public boolean isPerformNotificationAsync() {
        return performNotificationAsync;
    }

    public void setPerformNotificationAsync(boolean performNotificationAsync) {
        this.performNotificationAsync = performNotificationAsync;
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

    @Override
    public String toString() {
        return "OnlineAPIConfiguration{" +
                "activityUpdateInterval=" + activityUpdateInterval +
                ", performLoginNotificationOnActivityOfflineErrors=" + performLoginNotificationOnActivityOfflineErrors +
                ", performActivityUpdateOnLoginOnlineErrors=" + performActivityUpdateOnLoginOnlineErrors +
                ", performNotificationAsync=" + performNotificationAsync +
                ", activityEventProcessorSleepTime=" + activityEventProcessorSleepTime +
                ", activityEventProcessorSize=" + activityEventProcessorSize +
                ", activityEventProcessorChannelsAmount=" + activityEventProcessorChannelsAmount +
                '}';
    }


}
