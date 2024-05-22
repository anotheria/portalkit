package net.anotheria.portalkit.adminapi.rest.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthAPI;
import net.anotheria.portalkit.adminapi.rest.ErrorKey;
import net.anotheria.portalkit.adminapi.rest.ReplyObject;
import net.anotheria.portalkit.adminapi.rest.auth.request.LoginRequest;

@Path("admin-api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v1/")
public class AuthResource {

    private AdminAuthAPI authAPI;

    public AuthResource() {
        this.authAPI = APIFinder.findAPI(AdminAuthAPI.class);
    }

    @GET
    @Operation(description = "Internal Admin-API endpoint to get current logged in admin-user.")
    @Path("me")
    @ApiResponse(
            description = "Admin-user login.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    public Response getCurrentUser() {
        String login = null;
        try {
            String currentToken = (String) APICallContext.getCallContext().getAttribute("AUTH_TOKEN");
            login = authAPI.authenticateByToken(currentToken);
        } catch (AdminAPIAuthenticationException ex) {
            return Response.status(401).entity(ReplyObject.error(ErrorKey.INVALID_TOKEN)).build();
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("login", login)).build();
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
