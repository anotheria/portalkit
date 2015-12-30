package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * {@link AccountSettingsService} exception class.
 * 
 * @author lrosenberg
 * @since 11.12.12 13:17
 */
public class AccountSettingsServiceException extends PortalKitServiceException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 6510084249122635994L;

	/**
	 * 
	 * @param message
	 */
	public AccountSettingsServiceException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public AccountSettingsServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
