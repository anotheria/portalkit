package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * Base exception for the account service.
 *
 * @author lrosenberg
 * @since 11.12.12 11:09
 */
public class AccountAdminServiceException extends PortalKitServiceException {
	public AccountAdminServiceException(String message){
		super(message);
	}

	public AccountAdminServiceException(String message, Throwable cause){
		super(message, cause);
	}

	public AccountAdminServiceException(AccountPersistenceServiceException exception){
		super("Persistence failure", exception);
	}
}
