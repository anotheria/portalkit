package net.anotheria.portalkit.services.foreignid.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * ForeignIdPersistenceServiceException class.
 * 
 * @author lrosenberg
 * @since 28.12.12 23:47
 */
public class ForeignIdPersistenceServiceException extends PortalKitPersistenceServiceException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1634078272071530473L;

	/**
	 * With message.
	 * @param message
	 */
	public ForeignIdPersistenceServiceException(String message) {
		super(message);
	}

	/**
	 * With message and cause exception.
	 * @param message
	 * @param cause
	 */
	public ForeignIdPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
