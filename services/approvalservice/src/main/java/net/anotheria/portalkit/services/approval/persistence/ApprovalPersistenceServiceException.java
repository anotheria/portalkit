package net.anotheria.portalkit.services.approval.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * Approval persistence service exception class.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalPersistenceServiceException extends PortalKitPersistenceServiceException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 2237340710234910195L;

	/**
	 * Constructor with message.
	 *
	 * @param message fail message
	 */
	public ApprovalPersistenceServiceException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and {@link Throwable}.
	 *
	 * @param message fail message
	 * @param cause fail cause
	 */
	public ApprovalPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
