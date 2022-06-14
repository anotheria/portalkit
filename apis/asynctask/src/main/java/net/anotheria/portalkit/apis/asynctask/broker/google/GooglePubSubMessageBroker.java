package net.anotheria.portalkit.apis.asynctask.broker.google;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskMessageBroker;
import net.anotheria.portalkit.apis.asynctask.broker.amazon.AmazonSqsMessageBroker;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class GooglePubSubMessageBroker implements AsyncTaskMessageBroker {

    private static final Logger log = LoggerFactory.getLogger(AmazonSqsMessageBroker.class);

    private final Map<String, AsyncTaskConfig> taskConfigByType;

    GooglePubSubMessageBroker(Map<String, AsyncTaskConfig> taskConfigByType) {
        this.taskConfigByType = taskConfigByType;
    }

    @Override
    public void send(AsyncTask asyncTask) throws APIException {
        GooglePubSubConfig config = GooglePubSubConfig.getInstance();
    }

    @Override
    public List<AsyncTask> getTasks() throws APIException {
        return null;
    }
}
