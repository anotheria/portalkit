package net.anotheria.portalkit.adminapi.rest.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import net.anotheria.anoplass.api.APIException;
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
    @Operation(description = "Returns account info by accountId")
    @Path("/{accountId}")
    @ApiResponse(
            description = "Account object.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAccountAO.class)))
    public Response getAccountById(@PathParam("accountId") @Parameter(name = "accountId", description = "Account to get") String accountId) {
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
    @Operation(description = "Update account information. Payload accepts email, tenant, brand, name, type.",
            requestBody = @RequestBody(description = "Payload to update account object information.<br>" +
                    "Only ID field is required, other fields are not-required<br>" +
                    "Only a not-null field will be updated in the target account object.<br>" +
                    "For example if only 'email' field is present in payload, then only email will be updated. Another fields leave as before.",
                    content = @Content(schema = @Schema(implementation = AccountUpdateRequest.class))))
    @ApiResponse(
            description = "Updated account object.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAccountAO.class)))
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
    @Operation(description = "Adds account status to given account.")
    @Path("add-status/{accountId}/{status}")
    @ApiResponse(
            description = "Updated account object.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAccountAO.class)))
    public Response addStatusToAccount(@PathParam("accountId") @Parameter(description = "Account to add new status") String accountId, @PathParam("status") @Parameter(description = "Status to add") int status) {
        AdminAccountAO result = null;
        try {
            result = adminAPI.addAccountStatus(new AccountId(accountId), status);
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Operation(description = "Removes account status from given account.")
    @Path("remove-status/{accountId}/{status}")
    @ApiResponse(
            description = "Updated account object.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminAccountAO.class)))
    public Response removeStatusFromAccount(@PathParam("accountId") @Parameter(description = "Account from where remove a status") String accountId, @PathParam("status") @Parameter(description = "Status to remove") int status) {
        AdminAccountAO result = null;
        try {
            result = adminAPI.removeAccountStatus(new AccountId(accountId), status);
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("data", result)).build();
    }

    @POST
    @Operation(description = "Changes account password.", requestBody = @RequestBody(description = "Payload to change account password.",
            content = @Content(schema = @Schema(implementation = AccountSetPasswordRequest.class))))
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
    @Operation(description = "Get auth token to login as provided account.")
    @Path("sign-as/{accountId}")
    @ApiResponse(
            description = "Auth token.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    public Response getSignAsToken(@PathParam("accountId") @Parameter(description = "account whose token will be created to perform log in") String accountId) {
        String result = null;
        try {
            result = adminAPI.getSignAsToken(new AccountId(accountId));
        } catch (Exception any) {
            return Response.status(500).entity(ReplyObject.error(any)).build();
        }
        return Response.status(200).entity(ReplyObject.success("token", result)).build();
    }

}
