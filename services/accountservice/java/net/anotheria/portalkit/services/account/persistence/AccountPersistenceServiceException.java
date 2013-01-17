package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * Base exception class for exceptions in accountpersistenceservice.
 *
 * @author lrosenberg
 * @since 12.12.12 11:41
 */
public class AccountPersistenceServiceException extends PortalKitPersistenceServiceException {
	public AccountPersistenceServiceException(String message) {
		super(message);
	}

	public AccountPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
