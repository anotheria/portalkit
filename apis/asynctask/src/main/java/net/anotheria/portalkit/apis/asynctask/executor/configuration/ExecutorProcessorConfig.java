package net.anotheria.portalkit.apis.asynctask.executor.configuration;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Async-executors configuration.
 *
 * @author ykalapusha
 */
@ConfigureMe(name = "pk-async-executor-config")
public class ExecutorProcessorConfig implements Serializable {
    /**
     * Serial version UID.
     */
    @DontConfigure
    private static final long serialVersionUID = -3883798936552117594L;
    /**
     * {@link Logger} instance.
     */
    @DontConfigure
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorProcessorConfig.class);
    /**
     * Synchronization lock.
     */
    @DontConfigure
    private static final Object LOCK = new Object();
    /**
     * {@link ExecutorProcessorConfig} configured instance.
     */
    @DontConfigure
    private static volatile ExecutorProcessorConfig instance;
    /**
     * Async-executor {@link ExecutorProcessorConfiguration} configurations.
     */
    @Configure
    private ExecutorProcessorConfiguration[] executorProcessorConfigurations;
    /**
     * Map for {@link ExecutorProcessorConfiguration} of specified {@link String} task type.
     */
    @DontConfigure
    private ConcurrentMap<String, ExecutorProcessorConfiguration> configurationsMap;

    /**
     * Default constructor.
     */
    private ExecutorProcessorConfig() {
        try {
            ConfigurationManager.INSTANCE.configure(this);
            configureMap();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ExecutorConfig() configured with[" + this + "].");
        } catch (IllegalArgumentException e) {
            configureMap();
            LOGGER.warn("ExecutorConfig() configuration fail. Relaying on defaults[" + this.toString() + "].");
        }
    }

    /**
     * Get configuration instance.
     *
     * @return {@link ExecutorProcessorConfig} configured instance
     */
    public static ExecutorProcessorConfig getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ExecutorProcessorConfig();
                }
            }
        }
        return instance;
    }

    @AfterConfiguration
    public void configureMap() {
        configurationsMap = new ConcurrentHashMap<>();
        if (executorProcessorConfigurations != null) {
            for (ExecutorProcessorConfiguration executorProcessorConfiguration : executorProcessorConfigurations) {
                configurationsMap.put(executorProcessorConfiguration.getTaskType(), executorProcessorConfiguration);
            }
        }
    }

    /**
     * Returns {@link ExecutorProcessorConfiguration} for specified {@link String} task type.
     *
     * @param taskType {@link String} task type for configuration
     * @return {@link ExecutorProcessorConfiguration} for this {@link String} task type
     */
    public ExecutorProcessorConfiguration getExecutorProcessorConfiguration(String taskType) {
        ExecutorProcessorConfiguration configuration = configurationsMap.get(taskType);
        if (configuration == null) {
            configuration = ExecutorProcessorConfiguration.getDefaultConfiguration(taskType);
            configurationsMap.put(taskType, configuration);
        }
        return configuration;
    }

    public ExecutorProcessorConfiguration[] getExecutorProcessorConfigurations() {
        return executorProcessorConfigurations;
    }

    public void setExecutorProcessorConfigurations(ExecutorProcessorConfiguration[] executorProcessorConfigurations) {
        this.executorProcessorConfigurations = executorProcessorConfigurations;
    }

    @Override
    public String toString() {
        return "ExecutorConfig{" +
                "configurationsMap=" + configurationsMap +
                '}';
    }
}
