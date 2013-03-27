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

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 2236640256265161980L;

	/**
	 * Constructor with message.
	 * @param message
	 */
	public ForeignIdServiceException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and {@link Throwable}.
	 * @param message
	 * @param cause
	 */
	public ForeignIdServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor that transform message from persistence exception to the current.
	 * @param exception
	 */
	public ForeignIdServiceException(ForeignIdPersistenceServiceException exception) {
		super("Persistence failure", exception);
	}
}
