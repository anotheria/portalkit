package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;

/**
 * ForeignID service exception class.
 * 
 * @author lrosenberg
 * @since 28.12.12 23:45
 */
public class ForeignIdServiceException extends PortalKitServiceException {

	private static final long serialVersionUID = 1L;

	public ForeignIdServiceException(String message) {
		super(message);
	}

	public ForeignIdServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForeignIdServiceException(ForeignIdPersistenceServiceException exception) {
		super("Persistence failure", exception);
	}
}
