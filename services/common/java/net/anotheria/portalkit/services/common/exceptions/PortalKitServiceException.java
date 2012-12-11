package net.anotheria.portalkit.services.common.exceptions;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 11:09
 */
public class PortalKitServiceException extends Exception {
	public PortalKitServiceException(String message){
		super(message);
	}

	public PortalKitServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
