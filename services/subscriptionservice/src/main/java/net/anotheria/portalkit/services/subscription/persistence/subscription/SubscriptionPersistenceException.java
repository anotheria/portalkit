package net.anotheria.portalkit.services.subscription.persistence.subscription;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * @author Vlad Lukjanenko
 */
public class SubscriptionPersistenceException extends PortalKitServiceException {

    public SubscriptionPersistenceException(String message) {
        super(message);
    }

    public SubscriptionPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
