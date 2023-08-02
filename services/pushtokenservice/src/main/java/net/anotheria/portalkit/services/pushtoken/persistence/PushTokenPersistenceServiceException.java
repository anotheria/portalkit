package net.anotheria.portalkit.services.pushtoken.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

public class PushTokenPersistenceServiceException extends PortalKitPersistenceServiceException {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 1634078272071530473L;

    public PushTokenPersistenceServiceException(String message) {
        super(message);
    }

    public PushTokenPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
