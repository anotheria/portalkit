package net.anotheria.portalkit.services.match;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * @author bvanchuhov
 */
public class MatchServiceException extends PortalKitServiceException {

    public MatchServiceException(String message) {
        super(message);
    }

    public MatchServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
