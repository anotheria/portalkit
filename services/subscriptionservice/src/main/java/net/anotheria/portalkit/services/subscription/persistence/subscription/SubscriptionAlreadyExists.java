package net.anotheria.portalkit.services.subscription.persistence.subscription;

import java.text.MessageFormat;

/**
 * @author Vlad Lukjanenko
 */
public class SubscriptionAlreadyExists extends SubscriptionPersistenceException {

    public SubscriptionAlreadyExists(String subscriptionId) {
        super(MessageFormat.format("Subscription {0} already exists", subscriptionId));
    }

    public SubscriptionAlreadyExists(SubscriptionDO subscription) {
        this(subscription.getSubscriptionId());
    }
}