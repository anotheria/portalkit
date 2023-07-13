package net.anotheria.portalkit.adminapi.resources.dataspace;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.adminapi.api.AdminAPI;
import net.anotheria.portalkit.adminapi.api.AdminAPIFactory;
import net.anotheria.portalkit.adminapi.resources.ReplyObject;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.common.AccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("admin-api/dataspace")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataspaceResource {

    private static final Logger log = LoggerFactory.getLogger(DataspaceResource.class);

    private AccountSettingsService accountSettingsService;
    private AdminAPI adminAPI;

    public DataspaceResource() {
        this.adminAPI = AdminAPIFactory.getInstance();

        try {
            this.accountSettingsService = MetaFactory.get(AccountSettingsService.class);
        } catch (MetaFactoryException ex) {
            log.error("Cannot initialize DataspaceResource", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @GET
    @Path("{accountId}")
    public Response getUserDataspaces(@PathParam("accountId") String accountId) {

        List<Dataspace> result = null;
        try {
            result = adminAPI.getAllDataspaces(new AccountId(accountId));
        } catch (Exception e) {
            return Response.status(500).entity(ReplyObject.error(e)).build();
        }

        return Response.status(201).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Path("add-attribute")
    public Response addDataspaceAttribute(AddDataspaceAttributeRequest request) {
        Dataspace result = null;
        try {
            result = adminAPI.addDataspaceAttribute(new AccountId(request.getAccountId()), request.getDataspaceId(), request.getAttributeName(), request.getAttributeValue(), request.getType());
        } catch (Exception ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Path("remove-attribute")
    public Response removeDataspaceAttribute(RemoveDataspaceAttributeRequest request) {
        Dataspace result = null;
        try {
            result = adminAPI.removeDataspaceAttribute(new AccountId(request.getAccountId()), request.getDataspaceId(), request.getAttributeName());
        } catch (Exception ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }
}
