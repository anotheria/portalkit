package net.anotheria.portalkit.adminapi.rest.auth;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthAPI;
import net.anotheria.portalkit.adminapi.rest.ErrorKey;
import net.anotheria.portalkit.adminapi.rest.ReplyObject;
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
public class AuthResource {

    private AdminAuthAPI authAPI;

    public AuthResource() {
        this.authAPI = APIFinder.findAPI(AdminAuthAPI.class);
    }

    @POST
    @Path("login")
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
