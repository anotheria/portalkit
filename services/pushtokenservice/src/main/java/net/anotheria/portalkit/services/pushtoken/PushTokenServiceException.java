package net.anotheria.portalkit.services.pushtoken;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

public class PushTokenServiceException extends PortalKitServiceException {

    public PushTokenServiceException(String message) {
        super(message);
    }

    public PushTokenServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
