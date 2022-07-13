package net.anotheria.portalkit.apis.asynctask.broker.google;

import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import net.anotheria.anoplass.api.APIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public final class GooglePubSubSubscribers {

    private static final ConcurrentHashMap<String, SubscriberStub> subscribers = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GooglePubSubSubscribers.class);
    private static GooglePubSubSubscribers INSTANCE;

    private SubscriberStubSettings subscriberStubSettings;

    public GooglePubSubSubscribers() {
        GooglePubSubConfig config = GooglePubSubConfig.getInstance();

        try {
            subscriberStubSettings = SubscriberStubSettings.newBuilder()
                    .setTransportChannelProvider(
                            SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                                    .setMaxInboundMessageSize(config.getMaximumMessageSize())
                                    .build())
                    .build();
        } catch (Exception any) {
            log.error("Cannot init GooglePubSubSubscribers", any);
        }
    }

    public SubscriberStub getSubscriber(String subscriptionName) throws APIException {
        if (subscribers.containsKey(subscriptionName)) {
            return subscribers.get(subscriptionName);
        }

        try {
            SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings);
            subscribers.put(subscriptionName, subscriber);
            return subscriber;
        } catch (Exception any) {
            log.error("Cannot get subscriber", any);
            throw new APIException("Cannot get subscriber", any);
        }
    }

    public static GooglePubSubSubscribers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GooglePubSubSubscribers();
        }
        return INSTANCE;
    }

}
