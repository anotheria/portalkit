package net.anotheria.portalkit.services.relation.exception;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * @author bvanchuhov
 */
public class RelationServiceException extends PortalKitServiceException {

    public RelationServiceException(String message) {
        super(message);
    }

    public RelationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
