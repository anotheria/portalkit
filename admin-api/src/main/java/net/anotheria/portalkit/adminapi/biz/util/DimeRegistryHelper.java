package net.anotheria.portalkit.adminapi.biz.util;

import org.distributeme.core.conventions.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DimeRegistryHelper {

    private static final Logger log = LoggerFactory.getLogger(DimeRegistryHelper.class);

    public static void configureRemoteServiceDimeEventing() {
        if (SystemProperties.LOCAL_RMI_REGISTRY_PORT.isSet()) {
            int port = SystemProperties.LOCAL_RMI_REGISTRY_PORT.getAsInt();
            try {
                StartDistributeMeEventing.startDistributeMeEventing(port);
                log.info("Started DistributeMeEventing on port: {}", port);
            } catch (Exception e) {
                log.error("couldn't start DistributeMeEventing on port: {}", port);
            }
        }
    }
}
