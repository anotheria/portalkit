package net.anotheria.portalkit.adminapi.rest.dataspace;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.admin.AdminAPI;
import net.anotheria.portalkit.adminapi.api.admin.AdminAPIFactory;
import net.anotheria.portalkit.adminapi.rest.ReplyObject;
import net.anotheria.portalkit.adminapi.rest.dataspace.request.AddDataspaceAttributeRequest;
import net.anotheria.portalkit.adminapi.rest.dataspace.request.RemoveDataspaceAttributeRequest;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.common.AccountId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("admin-api/dataspace")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataspaceResource {

    private final AdminAPI adminAPI;

    public DataspaceResource() {
        this.adminAPI = APIFinder.findAPI(AdminAPI.class);
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
