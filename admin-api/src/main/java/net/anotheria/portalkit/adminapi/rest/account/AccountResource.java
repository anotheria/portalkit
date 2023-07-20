package net.anotheria.portalkit.adminapi.rest.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFactory;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anosite.cms.user.CMSUserManager;
import net.anotheria.portalkit.adminapi.api.admin.*;
import net.anotheria.portalkit.adminapi.api.shared.PageResult;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.ErrorKey;
import net.anotheria.portalkit.adminapi.rest.ReplyObject;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountSetPasswordRequest;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.services.common.AccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("admin-api/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v1/")
public class AccountResource {

    private static final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private AdminAPI adminAPI;

    public AccountResource() {
        this.adminAPI = APIFinder.findAPI(AdminAPI.class);
    }

    @GET
    @Path("statuses")
    @Operation(description = "Returns all account statuses")
    @ApiResponse(
            description = "List with account statuses.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAPIConfig.AccountStatusConfig.class)))
    public Response getAllAccountStatuses() {
        List<AdminAPIConfig.AccountStatusConfig> result = null;
        try {
            result = adminAPI.getAccountStatuses();
        } catch (APIException ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        boolean isKnown = CMSUserManager.getInstance().isKnownUser("admin");
        System.out.println(isKnown);
        return Response.status(200).entity(ReplyObject.success("success", result)).build();
    }

    @GET
    @Path("types")
    @Operation(description = "Returns all account types")
    @ApiResponse(
            description = "List with account types.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAPIConfig.AccountTypeConfig.class)))
    public Response getAllAccountTypes() {
        List<AdminAPIConfig.AccountTypeConfig> result = null;
        try {
            result = adminAPI.getAccountTypes();
        } catch (APIException ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }
        return Response.status(200).entity(ReplyObject.success("success", result)).build();
    }

    @GET
    @Operation(description = "Returns paginated list of accounts")
    @ApiResponse(
            description = "List with accounts.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAccountAO.class)))
    public Response getAllAccounts(@QueryParam("searchTerm") @Parameter(name = "searchTerm", description = "Term to filter accounts by email/name", required = true, schema = @Schema(implementation = String.class)) String searchTerm, @QueryParam("pageNumber") @Parameter(name = "pageNumber", description = "Number/index of page", required = true, schema = @Schema(implementation = Integer.class)) int pageNumber, @QueryParam("itemsOnPage") @Parameter(name = "itemsOnPage", description = "Amount on items on page", required = true, schema = @Schema(implementation = Integer.class)) int itemsOnPage) {
        PageResult<AdminAccountAO> result = null;
        try {
            result = adminAPI.getAccounts(pageNumber, itemsOnPage, searchTerm);
        } catch (APIException any) {
            log.error("Cannot get all accounts", any);
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccountById(@PathParam("accountId") String accountId) {
        AdminAccountAO result = null;
        try {
            result = adminAPI.getAccountById(new AccountId(accountId));
        } catch (Exception any) {
            log.error("Cannot get account by id", any);
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    public Response updateAccount(AccountUpdateRequest request) {
        AdminAccountAO result = null;

        try {
            result = adminAPI.updateAccount(request);
        } catch (AccountIdEmptyAdminAPIException idEmpty) {
            return Response.status(401).entity(ReplyObject.error(ErrorKey.ACCOUNT_ID_IS_EMPTY)).build();
        } catch (EmailExistsAdminAPIException emailExists) {
            return Response.status(401).entity(ReplyObject.error(ErrorKey.EMAIL_ALREADY_EXISTS)).build();
        } catch (APIException ex) {
            return Response.status(500).entity(ReplyObject.error(ex)).build();
        }

        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Path("add-status/{accountId}/{status}")
    public Response addStatusToAccount(@PathParam("accountId") String accountId, @PathParam("status") int status) {
        AdminAccountAO result = null;
        try {
            result = adminAPI.addAccountStatus(new AccountId(accountId), status);
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Path("remove-status/{accountId}/{status}")
    public Response removeStatusFromAccount(@PathParam("accountId") String accountId, @PathParam("status") int status) {
        AdminAccountAO result = null;
        try {
            result = adminAPI.removeAccountStatus(new AccountId(accountId), status);
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Path("password")
    public Response setNewPassword(AccountSetPasswordRequest request) {
        try {
            adminAPI.setNewAccountPassword(new AccountId(request.getAccountId()), request.getPassword());
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success()).build();
    }

    @GET
    @Path("sign-as/{accountId}")
    public Response getSignAsToken(@PathParam("accountId") String accountId) {
        String result = null;
        try {
            result = adminAPI.getSignAsToken(new AccountId(accountId));
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("token", result)).build();
    }

}
