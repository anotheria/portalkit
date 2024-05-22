package net.anotheria.portalkit.adminapi.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1/*")
@OpenAPIDefinition(info = @Info(title = "Portalkit Admin API", description = "API to perform admin actions", version = "v1"))
public class ServerConfig extends ResourceConfig {

    public ServerConfig() {
        packages(true, "net.anotheria.portalkit.adminapi.rest");
        register(new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        register(MultiPartFeature.class);
    }
}
