package net.anotheria.portalkit.services.accountlist.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * Base exception class for exceptions in accountlistpersistenceservice.
 * @author dagafonov
 *
 */
public class AccountListPersistenceServiceException extends PortalKitPersistenceServiceException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new AccountListPersistenceServiceException with a message.
	 * 
	 * @param message
	 */
	public AccountListPersistenceServiceException(String message) {
		super(message);
	}

	/**
	 * Creates a new AccountListPersistenceServiceException with a message and
	 * cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public AccountListPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
