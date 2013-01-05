package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 23:00
 */
public class PasswordPersistenceServiceException extends PortalKitPersistenceServiceException{
	public PasswordPersistenceServiceException(String message) {
		super(message);
	}

	public PasswordPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
