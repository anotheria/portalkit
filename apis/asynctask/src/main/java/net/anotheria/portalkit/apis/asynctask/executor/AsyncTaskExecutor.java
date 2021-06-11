package net.anotheria.portalkit.apis.asynctask.executor;

import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;

/**
 * Base interface to execute {@link AsyncTask}
 *
 * @author ynikonchuk
 */
public interface AsyncTaskExecutor {

    void executeTask(AsyncTask task);
}
