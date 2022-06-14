package net.anotheria.portalkit.apis.asynctask.broker.google;

import net.anotheria.portalkit.apis.asynctask.broker.amazon.AmazonSqsConfig;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigureMe(name = "pk-google-pub-sub-config")
public class GooglePubSubConfig {

    private static final Logger log = LoggerFactory.getLogger(AmazonSqsConfig.class);

    @Configure
    private String projectId;

    @Configure
    private String topicId;

    @Configure
    private String subscriptionId;

    @Configure
    private int maxMessagesPerPacket = 10;

    public static GooglePubSubConfig getInstance() {
        return GooglePubSubConfig.HolderClass.INSTANCE;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getMaxMessagesPerPacket() {
        return maxMessagesPerPacket;
    }

    public void setMaxMessagesPerPacket(int maxMessagesPerPacket) {
        this.maxMessagesPerPacket = maxMessagesPerPacket;
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
                "projectId='" + projectId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", maxMessagesPerPacket=" + maxMessagesPerPacket +
                '}';
    }
}
