package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 11.12.12 13:17
 */
public class AccountSettingsServiceException extends PortalKitServiceException{
	public AccountSettingsServiceException(String message) {
		super(message);
	}

	public AccountSettingsServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
