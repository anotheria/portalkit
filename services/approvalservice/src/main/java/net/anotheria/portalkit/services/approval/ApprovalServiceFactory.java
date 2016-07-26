package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link ApprovalService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalServiceFactory implements ServiceFactory<ApprovalService> {

	@Override
	public ApprovalService create() {
		return SpringHolder.get(ApprovalService.class);
	}
}
