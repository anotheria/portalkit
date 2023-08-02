package net.anotheria.portalkit.adminapi.biz.util;

import org.distributeme.core.RMIRegistryUtil;

public class StartDistributeMeEventing {
	public static final void startDistributeMeEventing(int port) throws Exception {
		RMIRegistryUtil.findOrCreateRegistry(port);
		org.distributeme.support.eventservice.generated.EventServiceRMIBridgeServer.init();
		org.distributeme.support.eventservice.generated.EventServiceRMIBridgeServer.createServiceAndRegisterLocally();

	}
}
