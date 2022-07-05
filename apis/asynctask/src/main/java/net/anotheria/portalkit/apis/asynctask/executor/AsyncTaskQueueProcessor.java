package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anoprise.processor.ElementWorker;
import net.anotheria.anoprise.processor.QueuedMultiProcessor;
import net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder;
import net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException;
import net.anotheria.anoprise.queue.BoundedFifoQueueFactory;
import net.anotheria.anoprise.queue.EnterpriseQueueFactory;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.portalkit.apis.asynctask.AsyncTaskAPI;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Periodically pulls async tasks and put them into the queue to process.
 * Tasks will be executed by proper registered {@link AsyncTaskExecutor}.
 *
 * @author ynikonchuk
 */
public class AsyncTaskQueueProcessor implements Runnable {

    /**
     * {@code Log4j} {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskQueueProcessor.class);
    /**
     * {@link OnDemandStatsProducer} of {@link AsyncTaskQueueProcessorStats}.
     */
    private final OnDemandStatsProducer<AsyncTaskQueueProcessorStats> producer;
    /**
     * {@link QueuedMultiProcessorBuilder} processor of {@link AsyncTask}.
     */
    private final QueuedMultiProcessor<AsyncTask> asyncTaskQueuedMultiProcessor;

    /**
     * {@link AsyncTaskAPI} instance.
     */
    private final AsyncTaskAPI asyncTaskAPI;

    /**
     * Executor by task type map.
     */
    private final Map<String, AsyncTaskExecutor> executorByTaskType;

    AsyncTaskQueueProcessor(Map<String, AsyncTaskExecutor> executorByTaskType) {
        this.executorByTaskType = executorByTaskType;

        this.asyncTaskAPI = APIFinder.findAPI(AsyncTaskAPI.class);

        producer = new OnDemandStatsProducer<>("AsyncTaskQueueProcessor", "business", "asynch", new AsyncTaskQueueProcessorStatsFactory());
        ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(producer);

        EnterpriseQueueFactory<AsyncTask> asyncTaskEnterpriseQueueFactory = new BoundedFifoQueueFactory<>();
        QueuedMultiProcessorBuilder<AsyncTask> builder = new QueuedMultiProcessorBuilder<>();

        AsyncTaskQueueProcessorConfig processorConfig = AsyncTaskQueueProcessorConfig.get();
        builder.setQueueSize(processorConfig.getQueueSize())
                .setProcessingLog(LOGGER)
                .setQueueFactoryClass((Class<EnterpriseQueueFactory<AsyncTask>>) asyncTaskEnterpriseQueueFactory.getClass())
                .setProcessorChannels(processorConfig.getProcessorThreadsCount()).attachMoskitoLoggers("AsyncTaskQueue", "storage", "default");

        asyncTaskQueuedMultiProcessor = builder.build("AsyncTaskQueuedProcessor", new AsyncExecutorProcessorWorker());
        asyncTaskQueuedMultiProcessor.start();
    }

    /**
     * Starts processor in own thread.
     */
    public void start() {
        Thread thread = new Thread(this);
        thread.setName("AsyncTaskQueueProcessorThread");
        thread.start();
    }

    @Override
    public void run() {
        List<AsyncTask> tasks = Collections.emptyList();
        while (true) {
            try {
                LOGGER.info("Trying to get async task (AsyncProcessor)...");
                //if the tasks is null, get new tasks, otherwise retry the last task.
                if (tasks.isEmpty()) {
                    tasks = asyncTaskAPI.getTasks();
                    LOGGER.info("Got async tasks: {}", tasks.size());
                }

                if (tasks.isEmpty()) {
                    sleepTime();
                    continue;
                }

                // trying to process tasks batch
                Iterator<AsyncTask> tasksIterator = tasks.iterator();
                while (tasksIterator.hasNext()) {
                    try {
                        AsyncTask task = tasksIterator.next();
                        LOGGER.info("add task to processing queue {}", task.getTaskType());
                        asyncTaskQueuedMultiProcessor.addToQueueDontWait(task);
                        // removed task if was added to processing queue
                        tasksIterator.remove();
                        producer.getDefaultStats().incTasks();
                    } catch (UnrecoverableQueueOverflowException e) {
                        LOGGER.error("Unable to add task to processor, queue is full. Sleeping...", e);
                        sleepTime();
                    }
                }
            } catch (Throwable e) {
                producer.getDefaultStats().incErrorTasks();
                LOGGER.error("Unable to execute async task", e);
                sleepTime();
            }
        }
    }

    private void sleepTime() {
        try {
            AsyncTaskQueueProcessorConfig processorConfig = AsyncTaskQueueProcessorConfig.get();
            Thread.sleep(TimeUnit.SECONDS.toMillis(processorConfig.getPullTasksPeriodSeconds()));
        } catch (InterruptedException ex) {
            LOGGER.error("InterruptedException in thread AsyncExecutorProcessor", ex);
        }
    }

    /**
     * Executes async task.
     *
     * @param task {@link AsyncTask} instance.
     */
    @Monitor
    private void executeTask(AsyncTask task) {
        LOGGER.info("STARTING executing task, " + task + " with age " + task.getAgeInMilliseconds() + " ms");
        long startTime = System.currentTimeMillis();
        AsyncTaskExecutor asyncTaskExecutor = executorByTaskType.get(task.getTaskType());
        if (asyncTaskExecutor == null) {
            LOGGER.warn("no AsyncTaskExecutor for task: " + task.getTaskType());
            return;
        }

        asyncTaskExecutor.executeTask(task);
        long endTime = System.currentTimeMillis();
        LOGGER.info("FINISHED executing task, " + task + " with age " + task.getAgeInMilliseconds() + " ms" + " in (" + (endTime - startTime) + ") ms");
    }

    /**
     * Send notifications.
     */
    private class AsyncExecutorProcessorWorker implements ElementWorker<AsyncTask> {
        @Override
        public void doWork(AsyncTask asyncTask) {
            try {
                executeTask(asyncTask);
                producer.getDefaultStats().incProcessedTasks();
            } catch (Throwable any) {
                LOGGER.error("AsyncExecutorProcessorWorker.doWork(" + asyncTask.getTaskType() + "), Can't process async task " + asyncTask.getTaskType() + ", ", any);
            }
        }
    }
}
