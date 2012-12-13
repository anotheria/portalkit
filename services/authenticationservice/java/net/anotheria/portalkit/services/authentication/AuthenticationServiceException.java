package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 15:56
 */
public class AuthenticationServiceException extends PortalKitServiceException{
	public AuthenticationServiceException(String message) {
		super(message);
	}

	public AuthenticationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationServiceException(AuthenticationPersistenceServiceException persistenceFailure){
		super("Failure in persistence", persistenceFailure);
	}
}
