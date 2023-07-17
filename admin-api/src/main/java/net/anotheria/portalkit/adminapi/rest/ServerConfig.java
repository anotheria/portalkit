package net.anotheria.portalkit.adminapi.rest;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1/*")
public class ServerConfig extends ResourceConfig {

    public ServerConfig() {
        packages(true, "net.anotheria.portalkit.adminapi.rest");
        register(MultiPartFeature.class);
    }
}
