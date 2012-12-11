package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;
/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 11:09
 */
public class AccountServiceException extends PortalKitServiceException {
	public AccountServiceException(String message){
		super(message);
	}

	public AccountServiceException(String message, Throwable cause){
		super(message, cause);
	}
}
