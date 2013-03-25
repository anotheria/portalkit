package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link ApprovalService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalServiceFactory implements ServiceFactory<ApprovalService> {

	@Override
	public ApprovalService create() {
		return new ApprovalServiceImpl();
	}

}
