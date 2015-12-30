package net.anotheria.portalkit.services.authentication.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 23:00
 */
public class AuthenticationPersistenceServiceException extends PortalKitPersistenceServiceException{
	public AuthenticationPersistenceServiceException(String message) {
		super(message);
	}

	public AuthenticationPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
