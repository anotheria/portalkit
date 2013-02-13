package net.anotheria.portalkit.services.record.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.02.13 00:26
 */
public class RecordPersistenceServiceException extends PortalKitPersistenceServiceException {
	public RecordPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordPersistenceServiceException(String message) {
		super(message);
	}
}
