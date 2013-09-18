package net.anotheria.portalkit.services.accountsettings.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 09:56
 */
public class AccountSettingsPersistenceServiceException extends PortalKitPersistenceServiceException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3363893040789508195L;

	public AccountSettingsPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountSettingsPersistenceServiceException(String message) {
		super(message);
	}
}
