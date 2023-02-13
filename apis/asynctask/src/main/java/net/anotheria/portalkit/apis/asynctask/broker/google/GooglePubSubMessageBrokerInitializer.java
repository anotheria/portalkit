package net.anotheria.portalkit.apis.asynctask.broker.google;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.SubscriptionName;
import com.google.pubsub.v1.Topic;
import com.google.pubsub.v1.TopicName;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GooglePubSubMessageBrokerInitializer {

    private static final Logger log = LoggerFactory.getLogger(GooglePubSubMessageBrokerInitializer.class);

    public static void initialize(Map<String, AsyncTaskConfig> taskConfigsByType) throws APIException {
        try {
            validateConfig();
        } catch (Exception any) {
            log.error(any.getMessage(), any);
            throw new APIException(any.getMessage(), any);
        }

        if (GooglePubSubConfig.getInstance().isAutoCreate()) {
            initializeTopics(taskConfigsByType);
            initializeSubscriptions(taskConfigsByType);
        }
    }

    private static void initializeTopics(Map<String, AsyncTaskConfig> taskConfigsByType) throws APIException {
        try {
            for (Map.Entry<String, AsyncTaskConfig> entry : taskConfigsByType.entrySet()) {
                for (String topic : GooglePubSubConfig.getInstance().getTopicPrefix()) {
                    createTopic(topic + "_" + entry.getKey());
                }
            }
        } catch (Exception any) {
            log.error("Cannot initialize topic", any);
            throw new APIException("Cannot initialize topic", any);
        }
    }

    private static void initializeSubscriptions(Map<String, AsyncTaskConfig> taskConfigsByType) throws APIException {
        try {
            for (Map.Entry<String, AsyncTaskConfig> entry : taskConfigsByType.entrySet()) {
                for (String subscriberName : GooglePubSubConfig.getInstance().getSubscriptionPrefix()) {
                    String subscriptionName = subscriberName + "_subscription_" + entry.getKey();
                    createPullSubscription(subscriptionName, entry.getKey());
                }
            }
        } catch (Exception any) {
            log.error("Cannot initialize subscriptions", any);
            throw new APIException("Cannot initialize subscriptions", any);
        }
    }

    // TOPICS RELATED METHODS -- START
    private static Set<String> getTopicsList() throws APIException {
        Set<String> result = new HashSet<>();
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            ProjectName projectName = ProjectName.of(GooglePubSubConfig.getInstance().getProjectId());
            for (Topic topic : topicAdminClient.listTopics(projectName).iterateAll()) {
                result.add(topic.getName());
            }
            return result;
        } catch (Exception any) {
            log.error("Cannot get list of topics", any);
            throw new APIException("Cannot get list of topics", any);
        }
    }

    private static void createTopic(String toCreate) throws APIException {
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            TopicName topicName = TopicName.of(GooglePubSubConfig.getInstance().getProjectId(), toCreate);
            try {
                topicAdminClient.getTopic(topicName);
            } catch (NotFoundException ex) {
                topicAdminClient.createTopic(topicName);
                log.info("Created topic: {}", toCreate);
            }
        } catch (Exception any) {
            log.error("Cannot create a topic", any);
            throw new APIException("Cannot create a topic", any);
        }
    }
    // TOPICS RELATED METHODS -- END

    // SUBSCRIPTIONS RELATED METHODS -- START
    private static Set<String> getSubscriptionsList() throws APIException {
        Set<String> result = new HashSet<>();
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            ProjectName projectName = ProjectName.of(GooglePubSubConfig.getInstance().getProjectId());
            for (Subscription subscription : subscriptionAdminClient.listSubscriptions(projectName).iterateAll()) {
                result.add(subscription.getName());
            }
            return result;
        } catch (Exception any) {
            log.error("Cannot get subscriptions list", any);
            throw new APIException("Cannot get subscriptions list", any);
        }
    }

    private static void createPullSubscription(String toCreate, String taskName) throws APIException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            SubscriptionName subscriptionName = SubscriptionName.of(GooglePubSubConfig.getInstance().getProjectId(), toCreate);
            try {
                subscriptionAdminClient.getSubscription(subscriptionName);
            } catch (NotFoundException ex) {
                for (String topic : GooglePubSubConfig.getInstance().getTopicPrefix()) {
                    TopicName topicName = TopicName.of(GooglePubSubConfig.getInstance().getProjectId(), topic + "_" + taskName);

                    Subscription subscription = subscriptionAdminClient.createSubscription(
                            subscriptionName, topicName, PushConfig.getDefaultInstance(), 10
                    );
                    log.info("Created pull subscription with name: {}", subscription.getName());
                }
            }
        } catch (Exception any) {
            log.error("Cannot create pull subscription", any);
            throw new APIException("Cannot create pull subscription", any);
        }
    }
    // SUBSCRIPTIONS RELATED METHODS -- END

    private static void validateConfig() {
        GooglePubSubConfig config = GooglePubSubConfig.getInstance();
        if (config.getTopicPrefix() == null || config.getTopicPrefix().length == 0) {
            throw new IllegalArgumentException("Topic prefix is null. Please, check config file");
        }
        if (config.getSubscriptionPrefix() == null || config.getSubscriptionPrefix().length == 0) {
            throw new IllegalArgumentException("Subscription prefix is null. Please, check config file");
        }
    }

}
