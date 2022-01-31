package net.anotheria.portalkit.services.session.bean;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

public class SessionNotFoundException extends PortalKitServiceException {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -827449733732617330L;

    /**
     * @param message
     */
    public SessionNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
