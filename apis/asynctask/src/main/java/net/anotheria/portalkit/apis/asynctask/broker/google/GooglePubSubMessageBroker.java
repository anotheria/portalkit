package net.anotheria.portalkit.apis.asynctask.broker.google;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskMessageBroker;
import net.anotheria.portalkit.apis.asynctask.broker.amazon.AmazonSqsMessageBroker;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTask;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskDeserializer;
import net.anotheria.portalkit.apis.asynctask.task.AsyncTaskSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GooglePubSubMessageBroker implements AsyncTaskMessageBroker {

    private static final Logger log = LoggerFactory.getLogger(AmazonSqsMessageBroker.class);

    private final Map<String, AsyncTaskConfig> taskConfigByType;
    private final String environmentName;

    GooglePubSubMessageBroker(Map<String, AsyncTaskConfig> taskConfigByType, String environmentName) {
        this.taskConfigByType = taskConfigByType;
        this.environmentName = environmentName;
        try {
            GooglePubSubMessageBrokerInitializer.initialize(taskConfigByType, environmentName);
        } catch (Exception any) {
            log.error("Cannot initialize GooglePubSubMessageBroker", any);
        }
    }

    @Override
    public void send(AsyncTask asyncTask) throws APIException {
        log.info("sendInternal({}) started", asyncTask.getTaskType());
        try {
            GooglePubSubConfig config = GooglePubSubConfig.getInstance();
            TopicName topicName = TopicName.of(config.getProjectId(), environmentName + "_" + asyncTask.getTaskType());

            Publisher publisher = null;
            try {
                publisher = Publisher.newBuilder(topicName).build();

                AsyncTaskConfig asyncTaskConfig = taskConfigByType.get(asyncTask.getTaskType());
                if (asyncTaskConfig == null) {
                    throw new APIException("no asyncTaskConfig for task: " + asyncTask.getTaskType());
                }

                AsyncTaskSerializer serializer = asyncTaskConfig.getSerializer();
                if (serializer == null) {
                    throw new APIException("no serializer for task: " + asyncTask.getTaskType());
                }

                ByteString data = ByteString.copyFromUtf8(serializer.serialize(asyncTask));
                Map<String, String> messageAttributes = new HashMap<>(1);
                messageAttributes.put("taskType", asyncTask.getTaskType());

                PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                        .putAllAttributes(messageAttributes)
                        .setData(data)
                        .build();

                try {
                    ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                    log.info("Published message {}", messageIdFuture.get());
                } catch (Exception any) {
                    log.error("ERROR, cannot publish a message {}. \n{}", asyncTask, any.getMessage());
                }
            } finally {
                if (publisher != null) {
                    publisher.shutdown();
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                }
            }
        } catch (Exception any) {
            throw new APIException(any.getMessage(), any);
        }
    }

    @Override
    public List<AsyncTask> getTasks() throws APIException {
        try {
            List<AsyncTask> result = new LinkedList<>();
            GooglePubSubConfig config = GooglePubSubConfig.getInstance();

            SubscriberStubSettings subscriberStubSettings =
                    SubscriberStubSettings.newBuilder()
                            .setTransportChannelProvider(
                                    SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                                            .setMaxInboundMessageSize(20 * 1024 * 1024) // 20MB (maximum message size).
                                            .build())
                            .build();

            for (Map.Entry<String, AsyncTaskConfig> entry : taskConfigByType.entrySet()) {
                try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
                    String subscriptionName = ProjectSubscriptionName.format(
                            config.getProjectId(), environmentName + "_subscription_" + entry.getKey()
                    );
                    PullRequest pullRequest =
                            PullRequest.newBuilder()
                                    .setMaxMessages(config.getMaxMessagesPerPacket())
                                    .setSubscription(subscriptionName)
                                    .build();

                    PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);

                    if (pullResponse.getReceivedMessagesList().isEmpty()) {
                        log.info("Message list is empty for {}", entry.getKey());
                        continue;
                    }

                    List<String> ackIds = new ArrayList<>();
                    for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
                        Map<String, String> messageAttributes = message.getMessage().getAttributesMap();
                        String taskType = messageAttributes.get("taskType");
                        if (taskType == null) {
                            continue;
                        }

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

                        result.add(deserializer.deserialize(message.getMessage().getData().toStringUtf8()));
                        ackIds.add(message.getAckId());
                    }

                    AcknowledgeRequest acknowledgeRequest =
                            AcknowledgeRequest.newBuilder()
                                    .setSubscription(subscriptionName)
                                    .addAllAckIds(ackIds)
                                    .build();

                    subscriber.acknowledgeCallable().call(acknowledgeRequest);
                }
            }
            return result;
        } catch (Exception any) {
            log.error("Cannot pull async tasks", any);
            throw new APIException(any.getMessage(), any);
        }
    }

}
