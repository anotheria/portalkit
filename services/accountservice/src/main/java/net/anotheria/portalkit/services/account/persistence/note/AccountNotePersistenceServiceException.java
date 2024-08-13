package net.anotheria.portalkit.services.account.persistence.note;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

public class AccountNotePersistenceServiceException extends PortalKitPersistenceServiceException {
    public AccountNotePersistenceServiceException(String message) {
        super(message);
    }

    public AccountNotePersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
