package net.anotheria.portalkit.adminapi.rest.dataspace;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.admin.AdminAPI;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceAO;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.ReplyObject;
import net.anotheria.portalkit.adminapi.rest.dataspace.request.AddDataspaceAttributeRequest;
import net.anotheria.portalkit.adminapi.rest.dataspace.request.CreateDataspaceRequest;
import net.anotheria.portalkit.adminapi.rest.dataspace.request.DeleteDataspaceRequest;
import net.anotheria.portalkit.adminapi.rest.dataspace.request.RemoveDataspaceAttributeRequest;
import net.anotheria.portalkit.services.common.AccountId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("admin-api/dataspace")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v1/")
public class DataspaceResource {

    private final AdminAPI adminAPI;

    public DataspaceResource() {
        this.adminAPI = APIFinder.findAPI(AdminAPI.class);
    }

    @GET
    @Operation(description = "Get all available dataspaces.")
    @Path("config")
    @ApiResponse(description = "Returns list of available configured dataspaces.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminAPIConfig.DataspaceConfig.class)))
    public Response getDataspacesConfig() {
        List<AdminAPIConfig.DataspaceConfig> result = null;
        try {
            result = adminAPI.getDataspaces();
        } catch (Exception e) {
            return Response.status(500).entity(ReplyObject.error(e)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @GET
    @Operation(description = "Get account's all dataspaces.")
    @Path("{accountId}")
    @ApiResponse(
            description = "List with dataspaces.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DataspaceAO.class)))
    public Response getUserDataspaces(@PathParam("accountId") @Parameter(description = "Dataspaces account-owner") String accountId) {

        List<DataspaceAO> result = null;
        try {
            result = adminAPI.getAllDataspaces(new AccountId(accountId));
        } catch (Exception e) {
            return Response.status(500).entity(ReplyObject.error(e)).build();
        }

        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Operation(description = "Create dataspace", requestBody = @RequestBody(description = "Payload to create dataspace",
            content = @Content(schema = @Schema(implementation = CreateDataspaceRequest.class))))
    @ApiResponse(description = "Created dataspace", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataspaceAO.class)))
    public Response createDataspace(CreateDataspaceRequest request) {
        DataspaceAO result = null;
        try {
            result = adminAPI.createDataspace(request.getAccountId(), request.getType(), request.getAttributes());
        } catch (Exception ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Operation(description = "Add attribute to provided dataspace.", requestBody = @RequestBody(description = "Payload to add new dataspace attribute",
            content = @Content(schema = @Schema(implementation = AddDataspaceAttributeRequest.class))))
    @Path("add-attribute")
    @ApiResponse(
            description = "Updated dataspace object.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DataspaceAO.class)))
    public Response addDataspaceAttribute(AddDataspaceAttributeRequest request) {
        DataspaceAO result = null;
        try {
            result = adminAPI.saveDataspaceAttribute(new AccountId(request.getAccountId()), request.getDataspaceId(), request.getAttributeName(), request.getAttributeValue(), request.getType());
        } catch (Exception ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Operation(description = "Remove attribute from provided dataspace.", requestBody = @RequestBody(description = "Payload to remove dataspace attribute",
            content = @Content(schema = @Schema(implementation = RemoveDataspaceAttributeRequest.class))))
    @Path("remove-attribute")
    @ApiResponse(
            description = "Updated dataspace object.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DataspaceAO.class)))
    public Response removeDataspaceAttribute(RemoveDataspaceAttributeRequest request) {
        DataspaceAO result = null;
        try {
            result = adminAPI.removeDataspaceAttribute(new AccountId(request.getAccountId()), request.getDataspaceId(), request.getAttributeName());
        } catch (Exception ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @DELETE
    @Operation(description = "Deletes dataspace.", requestBody = @RequestBody(description = "Payload to delete dataspace",
            content = @Content(schema = @Schema(implementation = DeleteDataspaceRequest.class))))
    @ApiResponse(
            description = "Success reply object",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReplyObject.class)))
    public Response deleteDataspace(DeleteDataspaceRequest request) {
        try {
            adminAPI.deleteDataspace(request.getAccountId(), request.getType());
        } catch (APIException ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success()).build();
    }
}
