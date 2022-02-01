package net.anotheria.portalkit.services.session.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

import java.io.Serializable;

public class SessionPersistenceServiceException extends PortalKitPersistenceServiceException implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2639626809979951647L;

    public SessionPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionPersistenceServiceException(String message) {
        super(message);
    }

}
