package net.anotheria.portalkit.services.approval;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * Approval service exception class.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalServiceException extends PortalKitServiceException {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 6248867053852579688L;

	/**
	 * Constructor with message.
	 * @param message
	 */
	public ApprovalServiceException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and {@link Throwable}.
	 * @param message
	 * @param cause
	 */
	public ApprovalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
