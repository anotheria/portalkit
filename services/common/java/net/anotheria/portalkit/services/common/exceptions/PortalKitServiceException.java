package net.anotheria.portalkit.services.common.exceptions;

/**
 * Base class for all service-base-exceptions.
 *
 * @author lrosenberg
 * @since 11.12.12 11:09
 */
public class PortalKitServiceException extends Exception {

	/**
	 * svuid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception.
	 * @param message message.
	 */
	public PortalKitServiceException(String message){
		super(message);
	}

	/**
	 * Creates a new exception with a cause.
	 * @param message exception message.
	 * @param cause the cause, exception that caused the exception.
	 */
	public PortalKitServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
