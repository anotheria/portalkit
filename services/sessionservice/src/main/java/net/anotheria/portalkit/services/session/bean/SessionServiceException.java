package net.anotheria.portalkit.services.session.bean;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

public class SessionServiceException extends PortalKitServiceException {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 4429823856355699788L;

    /**
     * @param message
     */
    public SessionServiceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public SessionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
