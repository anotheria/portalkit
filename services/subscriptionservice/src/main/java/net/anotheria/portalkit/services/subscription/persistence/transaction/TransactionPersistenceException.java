package net.anotheria.portalkit.services.subscription.persistence.transaction;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * @author Vlad Lukjanenko
 */
public class TransactionPersistenceException extends PortalKitServiceException {

    public TransactionPersistenceException(String message) {
        super(message);
    }

    public TransactionPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
