package net.anotheria.portalkit.apis.asynctask.executor.configuration;

import org.configureme.annotations.Configure;
import org.configureme.annotations.DontConfigure;

import java.io.Serializable;

/**
 * Configuration for Async-Executor based on Task type.
 *
 * @author ykalapusha
 */
public class ExecutorProcessorConfiguration implements Serializable {
    /**
     * Serial version UID.
     */
    @DontConfigure
    private static final long serialVersionUID = -4244109728950346662L;
    /**
     * Async-executor task name.
     */
    @Configure
    private String taskType;
    /**
     * {@link ExecutorProcessorType} of async-executor.
     */
    @Configure
    private ExecutorProcessorType executorProcessorType;

    /**
     * Number of executors per task (used only for {@link ExecutorProcessorType#MULTI_THREADED} executor type).
     */
    @Configure
    private int executorsPerTaskNumber;
    /**
     * Executor threads sleep time for async-executor in seconds.
     */
    @Configure
    private int executorThreadsSleepTimeInSeconds;
    /**
     * Queue size for multi processor.
     */
    @Configure
    private int executorQueueSize;

    /**
     * Default constructor.
     */
    public ExecutorProcessorConfiguration() {
    }

    public static ExecutorProcessorConfiguration getDefaultConfiguration(String taskType) {
        ExecutorProcessorConfiguration configuration = new ExecutorProcessorConfiguration();
        configuration.setTaskType(taskType);
        configuration.setExecutorProcessorType(ExecutorProcessorType.SINGLE);
        configuration.setExecutorThreadsSleepTimeInSeconds(10);
        return configuration;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public ExecutorProcessorType getExecutorProcessorType() {
        return executorProcessorType;
    }

    public void setExecutorProcessorType(ExecutorProcessorType executorProcessorType) {
        this.executorProcessorType = executorProcessorType;
    }

    public int getExecutorsPerTaskNumber() {
        return executorsPerTaskNumber;
    }

    public void setExecutorsPerTaskNumber(int executorsPerTaskNumber) {
        this.executorsPerTaskNumber = executorsPerTaskNumber;
    }

    public int getExecutorThreadsSleepTimeInSeconds() {
        return executorThreadsSleepTimeInSeconds;
    }

    public void setExecutorThreadsSleepTimeInSeconds(int executorThreadsSleepTimeInSeconds) {
        this.executorThreadsSleepTimeInSeconds = executorThreadsSleepTimeInSeconds;
    }

    public int getExecutorQueueSize() {
        return executorQueueSize;
    }

    public void setExecutorQueueSize(int executorQueueSize) {
        this.executorQueueSize = executorQueueSize;
    }

    @Override
    public String toString() {
        return "ExecutorProcessorConfiguration{" +
                "taskType='" + taskType + '\'' +
                ", executorProcessorType=" + executorProcessorType +
                ", executorsPerTaskNumber=" + executorsPerTaskNumber +
                ", executorThreadsSleepTimeInSeconds=" + executorThreadsSleepTimeInSeconds +
                ", executorQueueSize=" + executorQueueSize +
                '}';
    }
}
