package net.anotheria.portalkit.apis.asynctask;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.apis.asynctask.task.NoOperationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Sender of {@link NoOperationTask} to different async topics.
 *
 * @author lrosenberg
 * @since 2019-05-22 23:16
 */
public class NoOperationAsyncCommandScheduler implements Runnable {

    //actually we don't need to have this an instance, but i feel we might need it, so i keep it as variable for now.
    private static Executor executorService;
    /**
     * {@link Logger} instance.
     */
    private static Logger log = LoggerFactory.getLogger("AsyncCommandSchedulerLogger");
    /**
     * API: {@link AsyncTaskAPI}.
     */
    private AsyncTaskAPI asyncTaskAPI = APIFinder.findAPI(AsyncTaskAPI.class);
    /**
     * Name of topic which we used for send tasks.
     */
    private String topicName;

    /**
     * Default constructor.
     *
     * @param topicName topic name
     */
    public NoOperationAsyncCommandScheduler(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void run() {
        NoOperationTask task = new NoOperationTask(topicName);
        try {
            asyncTaskAPI.addTask(task);
            log.info("Sending synch task {}", task);
        }catch(APIException e){
            log.error("Can't push synch task {}", task, e);
        }
    }

    public static void startNoOperationAsyncCommandScheduler(String topicName) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        ((ScheduledExecutorService) executorService).scheduleAtFixedRate(
                new NoOperationAsyncCommandScheduler(topicName),
                60,
                60,
                TimeUnit.SECONDS
        );
    }
}
