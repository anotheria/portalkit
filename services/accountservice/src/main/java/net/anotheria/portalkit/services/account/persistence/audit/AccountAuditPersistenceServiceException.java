package net.anotheria.portalkit.services.account.persistence.audit;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * Base exception class for exceptions in {@link AccountAuditPersistenceService}.
 *
 * @author ykalapusha
 */
public class AccountAuditPersistenceServiceException extends PortalKitPersistenceServiceException {
    /**
     * Creates new {@link AccountAuditPersistenceServiceException} with message.
     *
     * @param message   error message
     */
    public AccountAuditPersistenceServiceException(String message) {
        super(message);
    }

    /**
     * Creates new {@link AccountAuditPersistenceServiceException} with message and cause.1
     *
     * @param message   error message
     * @param cause     {@link Throwable} error cause
     */
    public AccountAuditPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
