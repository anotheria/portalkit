package net.anotheria.portalkit.services.record.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * RecordPersistenceServiceException.
 * 
 * @author lrosenberg
 * @since 14.02.13 00:26
 */
public class RecordPersistenceServiceException extends PortalKitPersistenceServiceException {
	
	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -7459347680953414047L;

	public RecordPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordPersistenceServiceException(String message) {
		super(message);
	}
}
