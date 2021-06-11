package net.anotheria.portalkit.apis.asynctask.executor;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigureMe(name = "pk-async-task-queue-processor")
public class AsyncTaskQueueProcessorConfig {

    /**
     * {@code Log4j} {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskQueueProcessorConfig.class);

    /**
     * Period between pulling tasks from queue (seconds).
     */
    private long pullTasksPeriodSeconds = 10;

    /**
     * How many threads will process async task queue.
     */
    private int processorThreadsCount = 10;

    /**
     * Async tasks queue size.
     */
    private int queueSize = 30;


    public static AsyncTaskQueueProcessorConfig get() {
        return InstanceHolder.INSTANCE;
    }

    public long getPullTasksPeriodSeconds() {
        return pullTasksPeriodSeconds;
    }

    public void setPullTasksPeriodSeconds(long pullTasksPeriodSeconds) {
        this.pullTasksPeriodSeconds = pullTasksPeriodSeconds;
    }

    public int getProcessorThreadsCount() {
        return processorThreadsCount;
    }

    public void setProcessorThreadsCount(int processorThreadsCount) {
        this.processorThreadsCount = processorThreadsCount;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    static class InstanceHolder {
        private final static AsyncTaskQueueProcessorConfig INSTANCE;

        static {
            INSTANCE = new AsyncTaskQueueProcessorConfig();
            try {
                ConfigurationManager.INSTANCE.configure(INSTANCE);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("No async-task-queue-processor.json config file found. Continuing with defaults.");
            }
            LOGGER.info("Application configured: " + INSTANCE);
        }
    }

    @Override
    public String toString() {
        return "AsyncTaskQueueProcessorConfig{" +
                "pullTasksPeriodSeconds=" + pullTasksPeriodSeconds +
                ", processorThreadsCount=" + processorThreadsCount +
                ", queueSize=" + queueSize +
                '}';
    }
}
