package net.anotheria.portalkit.apis.asynctask.broker.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskMessageBroker;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskDeserializer;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmazonSqsMessageBroker implements AsyncTaskMessageBroker {

    private static final Logger log = LoggerFactory.getLogger(AmazonSqsMessageBroker.class);

    private final Map<String, AsyncTaskConfig> taskConfigByType;

    AmazonSqsMessageBroker(Map<String, AsyncTaskConfig> taskConfigByType) {
        this.taskConfigByType = taskConfigByType;
    }

    @Override
    public void send(AsyncTask asyncTask) throws APIException {
        AmazonSqsConfig config = AmazonSqsConfig.getInstance();

        AsyncTaskConfig asyncTaskConfig = taskConfigByType.get(asyncTask.getTaskType());
        if (asyncTaskConfig == null) {
            throw new APIException("no asyncTaskConfig for task: " + asyncTask.getTaskType());
        }

        AsyncTaskSerializer serializer = asyncTaskConfig.getSerializer();
        if (serializer == null) {
            throw new APIException("no serializer for task: " + asyncTask.getTaskType());
        }

        AmazonSQS client = getClient(config);
        String queueUrl = client.getQueueUrl(config.getQueueName()).getQueueUrl();

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>(1);
        messageAttributes.put("taskType", new MessageAttributeValue().withDataType("String").withStringValue(asyncTask.getTaskType()));

        AmazonSqsConfig brokerConfig = AmazonSqsConfig.getInstance();
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageAttributes(messageAttributes)
                .withMessageBody(serializer.serialize(asyncTask))
                .withDelaySeconds(brokerConfig.getMessageDelaySeconds());

        client.sendMessage(sendMessageRequest);
    }

    @Override
    public List<AsyncTask> getTasks(String topicName) throws APIException {
        AmazonSqsConfig config = AmazonSqsConfig.getInstance();

        AmazonSQS client = getClient(config);
        String queueUrl = client.getQueueUrl(config.getQueueName()).getQueueUrl();

        // receive messages from the queue
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        receiveMessageRequest.withMessageAttributeNames("taskType");

        List<Message> messages = client.receiveMessage(receiveMessageRequest).getMessages();

        List<AsyncTask> result = new ArrayList<>(messages.size());
        for (Message message : messages) {
            Map<String, MessageAttributeValue> messageAttributes = message.getMessageAttributes();
            MessageAttributeValue taskTypeAttr = messageAttributes.get("taskType");
            if (taskTypeAttr == null)
                continue;

            String taskType = taskTypeAttr.getStringValue();

            if (!taskType.equals(topicName))
                continue;

            AsyncTaskConfig asyncTaskConfig = taskConfigByType.get(taskType);
            if (asyncTaskConfig == null) {
                log.error("no asyncTaskConfig for task: " + taskType);
                continue;
            }

            AsyncTaskDeserializer deserializer = asyncTaskConfig.getDeserializer();
            if (deserializer == null) {
                log.error("no deserializer for task: " + taskType);
                continue;
            }

            result.add(deserializer.deserialize(message.getBody()));
            // remove message if successfully read
            client.deleteMessage(queueUrl, message.getReceiptHandle());
        }

        return result;
    }

    private AmazonSQS getClient(AmazonSqsConfig config) {
        if (config.isUseEnvironmentCredentials()) {
            return AmazonSQSClientBuilder.defaultClient();
        }
        AWSCredentials credentials = getCredentials(config);
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(config.getRegion()))
                .build();
    }

    private AWSCredentials getCredentials(AmazonSqsConfig config) {
        return new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
    }
}
