package net.anotheria.portalkit.adminapi.dataspace;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("admin-api/dataspace")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataspaceResource {

    private static final Logger log = LoggerFactory.getLogger(DataspaceResource.class);

    private AccountSettingsService accountSettingsService;
    private Gson gson;
    private GsonBuilder builder;

    public DataspaceResource() {
        this.builder = new GsonBuilder();
        this.builder.setPrettyPrinting();
        this.gson = builder.create();

        try {
            this.accountSettingsService = MetaFactory.get(AccountSettingsService.class);
        } catch (MetaFactoryException ex) {
            log.error("Cannot initialize DataspaceResource", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @GET
    @Path("{accountId}/{dataspace}")
    public Response getUserDataspace(@PathParam("accountId") String accountId, @PathParam("dataspace") String dataspace) {

        List<Dataspace> result = null;
        try {
            result = new LinkedList<>();
        } catch (Exception e) {
            log.error("Unable to get user's dataspace", e);
            return Response.status(500).build();
        }

        return Response.status(201).entity(gson.toJson(result)).build();
    }
}
