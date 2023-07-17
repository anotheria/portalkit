package net.anotheria.portalkit.adminapi.biz.util;

import net.anotheria.moskito.webui.embedded.MoSKitoInspectStartException;
import net.anotheria.moskito.webui.embedded.StartMoSKitoInspectBackendForRemote;
import org.distributeme.core.listener.ServerLifecycleListener;
import org.moskito.controlagent.endpoints.rmi.RMIEndpoint;
import org.moskito.controlagent.endpoints.rmi.RMIEndpointException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoSKitoRemoteInitializerListener implements ServerLifecycleListener{

	private static final Logger logger = LoggerFactory.getLogger(MoSKitoRemoteInitializerListener.class);

	@Override
	public void afterStart() {
		try {
			logger.info("Starting MoSKito Control RMI Endpoint");
			RMIEndpoint.startRMIEndpoint();
		}catch(RMIEndpointException e){
			logger.error("Can't start MoSKito Control RMI Endpoint", e);
		}

		String rmiPortProperty = System.getProperty("localRmiRegistryPort");
		if (rmiPortProperty != null && rmiPortProperty.length()>0) {
			int moskitoInspectPort = 0;
			try{
				moskitoInspectPort = Integer.parseInt(rmiPortProperty)+100;
			}catch(Exception any){
				logger.error("Can't start MoSKito Inspect Backend, can't parse rmiPort of the application "+rmiPortProperty, any);
			}
			if (moskitoInspectPort>100) {
				try {
					logger.info("Starting MoSKito Inspect Backend at port "+moskitoInspectPort);
					StartMoSKitoInspectBackendForRemote.startMoSKitoInspectBackend(moskitoInspectPort);
				} catch (MoSKitoInspectStartException e) {
					logger.error("Can't start MoSKito inspect backend");
				}
			}
		}
	}

	@Override
	public void beforeShutdown() {

	}
}
