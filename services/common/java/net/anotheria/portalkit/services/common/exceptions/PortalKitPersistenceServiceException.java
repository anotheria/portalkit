package net.anotheria.portalkit.services.common.exceptions;

/**
 * PortalKitPersistenceServiceException class.
 * 
 * @author lrosenberg
 * @since 12.12.12 10:11
 */
public class PortalKitPersistenceServiceException extends Exception {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 1329785239433539277L;

	public PortalKitPersistenceServiceException(String message) {
		super(message);
	}

	public PortalKitPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
