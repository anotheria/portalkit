package net.anotheria.portalkit.apis.asynctask.broker.google;

import net.anotheria.portalkit.apis.asynctask.broker.amazon.AmazonSqsConfig;
import net.anotheria.util.TimeUnit;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.AfterReConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigureMe(name = "pk-google-pub-sub-config")
public class GooglePubSubConfig {

    private static final Logger log = LoggerFactory.getLogger(AmazonSqsConfig.class);

    @Configure
    private boolean autoCreate = Boolean.TRUE;

    @Configure
    private String projectId;

    @Configure
    private String topicPrefix;

    @Configure
    private String subscriptionPrefix;

    @Configure
    private int maxMessagesPerPacket = 10;

    @Configure
    private int maximumMessageSize = 24 * 1024 * 1024;

    public static GooglePubSubConfig getInstance() {
        return GooglePubSubConfig.HolderClass.INSTANCE;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getSubscriptionPrefix() {
        return subscriptionPrefix + "_subscription";
    }

    public void setSubscriptionPrefix(String subscriptionPrefix) {
        this.subscriptionPrefix = subscriptionPrefix;
    }

    public int getMaxMessagesPerPacket() {
        return maxMessagesPerPacket;
    }

    public void setMaxMessagesPerPacket(int maxMessagesPerPacket) {
        this.maxMessagesPerPacket = maxMessagesPerPacket;
    }

    public int getMaximumMessageSize() {
        return maximumMessageSize;
    }

    public void setMaximumMessageSize(int maximumMessageSize) {
        this.maximumMessageSize = maximumMessageSize;
    }

    /**
     * Holder class idiom.
     */
    private static class HolderClass {
        /**
         * Singleton instance.
         */
        private static final GooglePubSubConfig INSTANCE;

        static {
            INSTANCE = new GooglePubSubConfig();
            try {
                ConfigurationManager.INSTANCE.configure(INSTANCE);
            } catch (Exception e) {
                log.error("GooglePubSubConfig configuration load failed: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return "GooglePubSubConfig{" +
                "autoCreate=" + autoCreate +
                ", projectId='" + projectId + '\'' +
                ", maxMessagesPerPacket=" + maxMessagesPerPacket +
                '}';
    }
}
