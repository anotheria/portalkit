package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.portalkit.apis.asynctask.AsyncTaskAPI;
import net.anotheria.portalkit.apis.asynctask.executor.monitoring.AsyncTaskQueueProcessorStats;
import net.anotheria.portalkit.apis.asynctask.executor.monitoring.AsyncTaskQueueProcessorStatsFactory;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Periodically pulls async tasks and process it by given {@link AsyncTaskExecutor}.
 *
 * @author ykalapusha
 */
public abstract class AsyncTaskExecutorProcessor implements Runnable {

    /**
     * {@link Logger instance}.
     */
    private final Logger logger;

    /**
     * Executor threads sleep time for async-executor in seconds.
     */
    private final long executorThreadsSleepTimeInSeconds;

    /**
     * {@link AsyncTaskExecutor} instance for processing tasks.
     */
    private final AsyncTaskExecutor asyncTaskExecutor;

    /**
     * API: {@link AsyncTaskAPI}.
     */
    protected final AsyncTaskAPI asyncTaskAPI;

    /**
     * {@link OnDemandStatsProducer} for moskito monitoring async task executor.
     */
    protected final OnDemandStatsProducer<AsyncTaskQueueProcessorStats> producer;

    /**
     * Task type (by default is topic name).
     */
    protected final String taskType;

    /**
     * Default constructor.
     *
     * @param taskType                              {@link String} name of task type
     * @param asyncTaskExecutor                     {@link AsyncTaskExecutor} executor for specified task type
     * @param executorThreadsSleepTimeInSeconds     time to sleep in seconds of this thread waiting for tasks
     * @param logger                                {@link Logger} instance
     */
    public AsyncTaskExecutorProcessor(String taskType, AsyncTaskExecutor asyncTaskExecutor, int executorThreadsSleepTimeInSeconds, Logger logger) {
        this.logger = logger;
        this.taskType = taskType;
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.executorThreadsSleepTimeInSeconds = executorThreadsSleepTimeInSeconds;

        asyncTaskAPI = APIFinder.findAPI(AsyncTaskAPI.class);

        producer = new OnDemandStatsProducer<>("AsyncTaskQueueProcessor_" + taskType, "business", "asynch", new AsyncTaskQueueProcessorStatsFactory());
        ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(producer);
    }

    protected void sleepTime() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(executorThreadsSleepTimeInSeconds));
        } catch (InterruptedException ex) {
            logger.error("InterruptedException in thread AsyncExecutorProcessor", ex);
        }
    }

    /**
     * Executes async task.
     *
     * @param task {@link AsyncTask} instance.
     */
    @Monitor
    protected void executeTask(AsyncTask task) {
        logger.info("STARTING executing task, " + task + " with age " + task.getAgeInMilliseconds() + " ms");
        long startTime = System.currentTimeMillis();
        asyncTaskExecutor.executeTask(task);
        long endTime = System.currentTimeMillis();
        logger.info("FINISHED executing task, " + task + " with age " + task.getAgeInMilliseconds() + " ms" + " in (" + (endTime - startTime) + ") ms");
    }
}
