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
    private String bootstrapServers;

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

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "KafkaConfig{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
