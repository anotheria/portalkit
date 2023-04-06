package net.anotheria.portalkit.apis.asynctask.broker.google;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import net.anotheria.anoplass.api.APIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class GooglePubSubPublishers {

    private static final ConcurrentHashMap<TopicName, Publisher> publishers = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GooglePubSubPublishers.class);

    private static GooglePubSubPublishers INSTANCE;

    public Publisher getPublisher(TopicName topicName) throws APIException {
        if (publishers.containsKey(topicName)) {
            return publishers.get(topicName);
        }

        try {
            Publisher publisherToAdd = Publisher.newBuilder(topicName).build();
            publishers.put(topicName, publisherToAdd);
            return publisherToAdd;
        } catch (Exception any) {
            log.error("Cannot get publisher", any);
            throw new APIException("Cannot get publisher", any);
        }
    }

    public static GooglePubSubPublishers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GooglePubSubPublishers();
        }
        return INSTANCE;
    }

    public void notifyShutdown(){
        for (Map.Entry<TopicName, Publisher> entry: publishers.entrySet()) {
            try {
                entry.getValue().shutdown();
                if (!entry.getValue().awaitTermination(5, TimeUnit.SECONDS)) {
                    log.error("The publisher did not terminate");
                }
            } catch (Exception e) {
                log.error("Unable to correct shutdown publisher. " + e.getMessage());
            }
        }
    }
}
