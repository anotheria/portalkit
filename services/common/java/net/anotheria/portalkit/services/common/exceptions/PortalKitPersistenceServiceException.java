package net.anotheria.portalkit.services.common.exceptions;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 10:11
 */
public class PortalKitPersistenceServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PortalKitPersistenceServiceException(String message){
		super(message);
	}

	public PortalKitPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
