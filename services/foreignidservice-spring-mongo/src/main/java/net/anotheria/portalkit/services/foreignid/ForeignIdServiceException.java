package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

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
}
