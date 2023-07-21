package net.anotheria.portalkit.adminapi.rest.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.admin.AdminAccountAO;
import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthAPI;
import net.anotheria.portalkit.adminapi.rest.ErrorKey;
import net.anotheria.portalkit.adminapi.rest.ReplyObject;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.adminapi.rest.auth.request.LoginRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin-api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v1/")
public class AuthResource {

    private AdminAuthAPI authAPI;

    public AuthResource() {
        this.authAPI = APIFinder.findAPI(AdminAuthAPI.class);
    }

    @POST
    @Operation(description = "Internal Admin-API login to call endpoints.", requestBody = @RequestBody(description = "Payload with login and password",
            content = @Content(schema = @Schema(implementation = LoginRequest.class))))
    @Path("login")
    @ApiResponse(
            description = "Auth token to set in 'authToken' header for every request.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    public Response login(LoginRequest request) {
        String token = null;
        try {
            token = authAPI.login(request.getLogin(), request.getPassword());
        } catch (AdminAPIAuthenticationException ex) {
            return Response.status(401).entity(ReplyObject.error(ErrorKey.NAME_OR_PASSWORD_MISMATCH)).build();
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("token", token)).build();
    }

    @POST
    @Operation(description = "Internal Admin-API logout.")
    @Path("logout")
    public Response logout() {
        try {
            authAPI.logout();
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success()).build();
    }
}
