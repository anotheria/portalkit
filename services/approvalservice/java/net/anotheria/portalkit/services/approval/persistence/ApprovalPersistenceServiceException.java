package net.anotheria.portalkit.services.approval.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

public class ApprovalPersistenceServiceException extends PortalKitPersistenceServiceException {
	
	private static final long serialVersionUID = 2237340710234910195L;

	public ApprovalPersistenceServiceException(String message) {
		super(message);
	}
	
	public ApprovalPersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
