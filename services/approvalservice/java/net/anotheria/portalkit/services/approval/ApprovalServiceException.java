package net.anotheria.portalkit.services.approval;

import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * Approval service exception class.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalServiceException extends PortalKitServiceException {

	private static final long serialVersionUID = 6248867053852579688L;

	public ApprovalServiceException(String message) {
		super(message);
	}

	public ApprovalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
