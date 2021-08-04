package net.anotheria.portalkit.apis.asynctask;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;

import java.util.List;

public interface AsyncTaskAPI extends API {

    /**
     * Adds new async task to queue.
     *
     * @param asyncTask
     *         async task.
     * @throws APIException
     *         if error occurs.
     */
    void addTask(AsyncTask asyncTask) throws APIException;

    /**
     * Returns tasks to process.
     *
     * @return list of {@link AsyncTask}
     * @throws APIException
     */
    List<AsyncTask> getTasks() throws APIException;
}
