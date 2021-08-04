package net.anotheria.portalkit.apis.asynctask.broker.kafka;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author ynikonchuk
 */
@ConfigureMe(name = "pk-kafka-config")
public final class KafkaConfig implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

    @DontConfigure
    private static KafkaConfig INSTANCE;

    @Configure
    private String producerHost;

    @Configure
    private int producerPort;

    @Configure
    private String consumerHost;

    @Configure
    private int consumerPort;

    @Configure
    private String groupId;

    /**
     * Default constructor.
     */
    private KafkaConfig() {

        try {
            ConfigurationManager.INSTANCE.configure(this);
        } catch (final IllegalArgumentException e) {
            LOGGER.warn("Configuration fail[" + e.getMessage() + "]. Relaying on defaults.");
        }
    }

    /**
     * Returns instance of {@link KafkaConfig}.
     */
    public static KafkaConfig getInstance() {

        if (INSTANCE == null) {

            synchronized (KafkaConfig.class) {

                if (INSTANCE == null) {
                    INSTANCE = new KafkaConfig();
                }
            }
        }

        return INSTANCE;
    }

    public String getProducerHost() {
        return producerHost;
    }

    public void setProducerHost(String producerHost) {
        this.producerHost = producerHost;
    }

    public int getProducerPort() {
        return producerPort;
    }

    public void setProducerPort(int producerPort) {
        this.producerPort = producerPort;
    }

    public String getConsumerHost() {
        return consumerHost;
    }

    public void setConsumerHost(String consumerHost) {
        this.consumerHost = consumerHost;
    }

    public int getConsumerPort() {
        return consumerPort;
    }

    public void setConsumerPort(int consumerPort) {
        this.consumerPort = consumerPort;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "KafkaConfig{" + "producerHost='" + producerHost + '\'' + ", producerPort=" + producerPort + ", consumerHost='" + consumerHost + '\'' + ", consumerPort=" + consumerPort +
                ", groupId='" + groupId + '\'' + '}';
    }
}
