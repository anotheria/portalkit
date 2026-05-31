package net.anotheria.portalkit.apis.asynctask.broker.google;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import net.anotheria.anoplass.api.APIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class GooglePubSubPublishers {

    private static final ConcurrentHashMap<TopicName, Publisher> publishers = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GooglePubSubPublishers.class);

    // volatile ensures the reference is safely published across threads
    private static volatile GooglePubSubPublishers INSTANCE;

    public Publisher getPublisher(TopicName topicName) throws APIException {
        // computeIfAbsent is atomic: at most one Publisher (and one gRPC channel) is created per topic
        try {
            return publishers.computeIfAbsent(topicName, key -> {
                try {
                    return Publisher.newBuilder(key).build();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            log.error("Cannot get publisher", e);
            throw new APIException("Cannot get publisher", e);
        }
    }

    public static GooglePubSubPublishers getInstance() {
        if (INSTANCE == null) {
            synchronized (GooglePubSubPublishers.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GooglePubSubPublishers();
                }
            }
        }
        return INSTANCE;
    }

    public void notifyShutdown(){
        for (Map.Entry<TopicName, Publisher> entry: publishers.entrySet()) {
            try {
                entry.getValue().shutdown();
                if (!entry.getValue().awaitTermination(1, TimeUnit.MINUTES))
                    log.error("The publisher [{}] did not terminate.", entry.getKey().getTopic());
                else
                    log.info("The publisher [{}] is turned off.", entry.getKey().getTopic());

            } catch (Exception e) {
                log.error("Unable to correct shutdown publisher. " + e.getMessage());
            }
        }
    }
}
