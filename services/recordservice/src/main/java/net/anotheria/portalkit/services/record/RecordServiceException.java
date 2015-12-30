package net.anotheria.portalkit.services.record;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * TODO comment this class
 * 
 * @author lrosenberg
 * @since 11.12.12 17:37
 */
public class RecordServiceException extends PortalKitServiceException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -5062726737051705965L;

	public RecordServiceException(String message) {
		super(message);
	}

	public RecordServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
