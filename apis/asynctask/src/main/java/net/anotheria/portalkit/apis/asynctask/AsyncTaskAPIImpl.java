package net.anotheria.portalkit.apis.asynctask;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskMessageBroker;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;

import java.util.List;

public class AsyncTaskAPIImpl extends AbstractAPIImpl implements AsyncTaskAPI {

    private final AsyncTaskMessageBroker messageBroker;

    public AsyncTaskAPIImpl(AsyncTaskMessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void addTask(AsyncTask asyncTask) throws APIException {
        messageBroker.send(asyncTask);
    }

    @Override
    public List<AsyncTask> getTasks() throws APIException {
        return messageBroker.getTasks();
    }
}
