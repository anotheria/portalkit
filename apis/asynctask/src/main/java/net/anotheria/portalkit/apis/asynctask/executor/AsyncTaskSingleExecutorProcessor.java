package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.portalkit.apis.asynctask.executor.configuration.ExecutorProcessorConfiguration;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Single implementation for {@link AsyncTaskExecutorProcessor}.
 *
 * @author ykalapusha
 */
public class AsyncTaskSingleExecutorProcessor extends AsyncTaskExecutorProcessor {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskSingleExecutorProcessor.class);

    /**
     * Constructor.
     *
     * @param asyncTaskExecutor     {@link AsyncTaskExecutor} executor for specified task type
     * @param configuration         {@link ExecutorProcessorConfiguration} of task type
     */
    public AsyncTaskSingleExecutorProcessor(AsyncTaskExecutor asyncTaskExecutor, ExecutorProcessorConfiguration configuration) {
        super(configuration.getTaskType(), asyncTaskExecutor, configuration.getExecutorThreadsSleepTimeInSeconds(), LOGGER);
    }

    @Override
    public void run() {
        List<AsyncTask> tasks = Collections.emptyList();
        while (true) {
            try {
                LOGGER.info("Trying to get async task (AsyncProcessor)...");
                //if the task is null, get new task, otherwise retry the last task.
                if (tasks.isEmpty()) {
                    tasks = asyncTaskAPI.getTasks();
                    LOGGER.info("Got async tasks: {}", tasks.size());
                }

                if (tasks.isEmpty()) {
                    sleepTime();
                } else {
                    Iterator<AsyncTask> taskIterator = tasks.iterator();
                    while (taskIterator.hasNext()) {
                        try {
                            AsyncTask task = taskIterator.next();
                            producer.getDefaultStats().incTasks();
                            executeTask(task);
                            producer.getDefaultStats().incProcessedTasks();
                            taskIterator.remove();
                        } catch (Exception any) {
                            LOGGER.error("Unable to execute task. {}. " + any.getMessage());
                        }
                    }
                }
            } catch (Throwable e) {
                producer.getDefaultStats().incErrorTasks();
                LOGGER.error("Unable to execute async task", e);
                sleepTime();
            }
        }
    }
}
