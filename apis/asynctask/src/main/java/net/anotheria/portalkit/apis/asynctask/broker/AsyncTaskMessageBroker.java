package net.anotheria.portalkit.apis.asynctask.broker;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;

import java.util.List;

public interface AsyncTaskMessageBroker {

    void send(AsyncTask asyncTask) throws APIException;

    List<AsyncTask> getTasks(String topicName) throws APIException;

    void notifyShutdown();
}
