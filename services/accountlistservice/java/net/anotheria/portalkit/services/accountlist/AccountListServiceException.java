package net.anotheria.portalkit.services.accountlist;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * AccountList service exception class.
 * 
 * @author dagafonov
 * 
 */
public class AccountListServiceException extends PortalKitServiceException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception.
	 * 
	 * @param message
	 *            message.
	 */
	public AccountListServiceException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception with a cause.
	 * 
	 * @param message
	 *            exception message.
	 * @param cause
	 *            the cause, exception that caused the exception.
	 */
	public AccountListServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
