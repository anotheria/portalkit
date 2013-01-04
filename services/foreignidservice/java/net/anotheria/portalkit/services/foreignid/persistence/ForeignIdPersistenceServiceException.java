package net.anotheria.portalkit.services.foreignid.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.12.12 23:47
 */
public class ForeignIdPersistenceServiceException extends PortalKitPersistenceServiceException{
	public ForeignIdPersistenceServiceException(String message) {
		super(message);
	}

	public ForeignIdPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
