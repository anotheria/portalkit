package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.anoprise.processor.ElementWorker;
import net.anotheria.anoprise.processor.QueuedMultiProcessor;
import net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder;
import net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException;
import net.anotheria.anoprise.queue.BoundedFifoQueueFactory;
import net.anotheria.anoprise.queue.EnterpriseQueueFactory;
import net.anotheria.portalkit.apis.asynctask.executor.configuration.ExecutorProcessorConfiguration;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Queue implementation for {@link AsyncTaskExecutorProcessor}.
 *
 * @author ykalapusha
 */
public class AsyncTaskQueueExecutorProcessor extends AsyncTaskExecutorProcessor {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskQueueExecutorProcessor.class);

    /**
     * {@link QueuedMultiProcessorBuilder} processor of {@link AsyncTask}.
     */
    private final QueuedMultiProcessor<AsyncTask> asyncTaskQueuedMultiProcessor;


    /**
     * Constructor.
     *
     * @param asyncTaskExecutor     {@link AsyncTaskExecutor} executor for specified task type
     * @param configuration         {@link ExecutorProcessorConfiguration} of task type
     */
    public AsyncTaskQueueExecutorProcessor(AsyncTaskExecutor asyncTaskExecutor, ExecutorProcessorConfiguration configuration) {
        super(configuration.getTaskType(), asyncTaskExecutor, configuration.getExecutorThreadsSleepTimeInSeconds(), LOGGER);

        EnterpriseQueueFactory<AsyncTask> asyncTaskEnterpriseQueueFactory = new BoundedFifoQueueFactory<>();
        QueuedMultiProcessorBuilder<AsyncTask> builder = new QueuedMultiProcessorBuilder<>();
        builder.setQueueSize(configuration.getExecutorQueueSize())
                .setQueueFactoryClass((Class<EnterpriseQueueFactory<AsyncTask>>) asyncTaskEnterpriseQueueFactory.getClass())
                .setProcessingLog(LOGGER)
                .setProcessorChannels(configuration.getExecutorsPerTaskNumber())
                .attachMoskitoLoggers("AsyncTaskQueue", "storage", "default");

        asyncTaskQueuedMultiProcessor = builder.build("AsyncTaskQueuedProcessor", new AsyncExecutorProcessorWorker());
        asyncTaskQueuedMultiProcessor.start();
    }

    @Override
    public void run() {
        List<AsyncTask> tasks = Collections.emptyList();
        while (true) {
            try {
                LOGGER.info("Trying to get async task (AsyncProcessor)...");
                //if the task is null, get new task, otherwise retry the last task.
                if (tasks.isEmpty()) {
                    tasks = asyncTaskAPI.getTasks(taskType);
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
