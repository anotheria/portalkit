package net.anotheria.portalkit.apis.asynctask;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.apis.asynctask.task.NoOperationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Sender of {@link NoOperationTask} to different async topics.
 *
 * @author lrosenberg
 * @since 2019-05-22 23:16
 */
public class NoOperationAsyncCommandScheduler {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("AsyncCommandSchedulerLogger");
    /**
     * API: {@link AsyncTaskAPI}.
     */
    private static final AsyncTaskAPI asyncTaskAPI = APIFinder.findAPI(AsyncTaskAPI.class);

    /**
     * Start sending {@link NoOperationTask} to specified {@code topicName}.
     *
     * @param topicName Topic name for sending {@link NoOperationTask}.
     */
    public static void startNoOperationAsyncCommandScheduler(String topicName) {
        Runnable task = () -> {
            NoOperationTask noOperationTask = new NoOperationTask(topicName);
            try {
                asyncTaskAPI.addTask(noOperationTask);
                LOGGER.info("Sending synch task {}", noOperationTask);
            }catch(APIException e){
                LOGGER.error("Can't push synch task {}", noOperationTask, e);
            }
        };

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(task, 60, 60, TimeUnit.SECONDS);
    }
}
