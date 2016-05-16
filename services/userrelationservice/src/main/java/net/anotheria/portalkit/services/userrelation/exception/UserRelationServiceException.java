package net.anotheria.portalkit.services.userrelation.exception;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * @author bvanchuhov
 */
public class UserRelationServiceException extends PortalKitServiceException {

    public UserRelationServiceException(String message) {
        super(message);
    }

    public UserRelationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
