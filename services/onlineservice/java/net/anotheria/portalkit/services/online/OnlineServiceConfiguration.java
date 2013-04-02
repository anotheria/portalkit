package net.anotheria.portalkit.services.online;

import net.anotheria.util.TimeUnit;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;

/**
 * Configuration for {@link OnlineService}, which allow to provide abd change basic settings.
 *
 * @author h3llka
 */
@ConfigureMe(name = "pk-online-service-config")
public final class OnlineServiceConfiguration {
    /**
     * Interval between  'inactive-users' cleanUp operations, interval is defined to be used as time in millis!
     * <a> 5 minutes by default</a>.
     */
    @Configure
    private long inactiveAccountsCleanUpInterval = TimeUnit.MINUTE.getMillis() * 5;
    /**
     * Interval during which online account stay active, during this time - account won't be cleanedUp as expired.
     * Defined to be used as time in millis!
     * <a> 10 minutes by default</a>.
     */
    @Configure
    private long maxAccountInactivityInterval = TimeUnit.MINUTE.getMillis() * 10;
    /**
     * Instance const.
     */
    @DontConfigure
    private static OnlineServiceConfiguration instance;
    /**
     * Sync monitor instance.
     */
    private static final Object lock = new Object();

    /**
     * Constructor.
     */
    private OnlineServiceConfiguration() {
        inactiveAccountsCleanUpInterval = TimeUnit.MINUTE.getMillis() * 5;
        maxAccountInactivityInterval = TimeUnit.MINUTE.getMillis() * 10;
    }

    /**
     * Return configured {@link OnlineServiceConfiguration} instance.
     *
     * @return {@link OnlineServiceConfiguration}
     */
    public static OnlineServiceConfiguration getInstance() {
        if (instance != null)
            return instance;

        synchronized (lock) {
            if (instance != null)
                return instance;

            instance = new OnlineServiceConfiguration();
            try {
                ConfigurationManager.INSTANCE.configure(instance);
            } catch (Exception e) {
                Logger.getLogger(OnlineServiceConfiguration.class).warn("Configuration failed, relying on defaults!");
            }
            return instance;
        }

    }


    public long getInactiveAccountsCleanUpInterval() {
        return inactiveAccountsCleanUpInterval;
    }

    public void setInactiveAccountsCleanUpInterval(long inactiveAccountsCleanUpInterval) {
        this.inactiveAccountsCleanUpInterval = inactiveAccountsCleanUpInterval;
    }

    public long getMaxAccountInactivityInterval() {
        return maxAccountInactivityInterval;
    }

    public void setMaxAccountInactivityInterval(long maxAccountInactivityInterval) {
        this.maxAccountInactivityInterval = maxAccountInactivityInterval;
    }

    @Override
    public String toString() {
        return "OnlineServiceConfiguration{" +
                "inactiveAccountsCleanUpInterval=" + inactiveAccountsCleanUpInterval +
                ", maxAccountInactivityInterval=" + maxAccountInactivityInterval +
                '}';
    }
}


