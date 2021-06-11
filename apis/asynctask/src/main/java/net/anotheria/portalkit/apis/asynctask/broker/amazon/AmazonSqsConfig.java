package net.anotheria.portalkit.apis.asynctask.broker.amazon;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigureMe(name = "pk-amazon-sqs-config")
public class AmazonSqsConfig {

    private static final Logger log = LoggerFactory.getLogger(AmazonSqsConfig.class);

    /**
     * If enabled amazon client will try to get credentials from node environment.
     */
    @Configure
    private boolean useEnvironmentCredentials;

    @Configure
    private String accessKey;

    @Configure
    private String secretKey;

    @Configure
    private String region;

    @Configure
    private String queueName;

    @Configure
    private int messageDelaySeconds = 5;


    public static AmazonSqsConfig getInstance() {
        return HolderClass.INSTANCE;
    }

    public boolean isUseEnvironmentCredentials() {
        return useEnvironmentCredentials;
    }

    public void setUseEnvironmentCredentials(boolean useEnvironmentCredentials) {
        this.useEnvironmentCredentials = useEnvironmentCredentials;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getMessageDelaySeconds() {
        return messageDelaySeconds;
    }

    public void setMessageDelaySeconds(int messageDelaySeconds) {
        this.messageDelaySeconds = messageDelaySeconds;
    }

    /**
     * Holder class idiom.
     */
    private static class HolderClass {
        /**
         * Singleton instance.
         */
        private static final AmazonSqsConfig INSTANCE;

        static {
            INSTANCE = new AmazonSqsConfig();
            try {
                ConfigurationManager.INSTANCE.configure(INSTANCE);
            } catch (Exception e) {
                log.error("AmazonSqsConfig configuration load failed: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return "AmazonSqsConfig{" +
                "devEnvironment=" + useEnvironmentCredentials +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", region='" + region + '\'' +
                ", queueName='" + queueName + '\'' +
                ", messageDelaySeconds=" + messageDelaySeconds +
                '}';
    }
}
