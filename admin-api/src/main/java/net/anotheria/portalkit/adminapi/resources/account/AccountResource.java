package net.anotheria.portalkit.adminapi.resources.account;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.api.*;
import net.anotheria.portalkit.adminapi.resources.ErrorKey;
import net.anotheria.portalkit.adminapi.resources.ReplyObject;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin-api/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private static final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private AdminAPI adminAPI;

    public AccountResource() {
        this.adminAPI = new AdminAPIImpl();
    }

    @GET
    @Path("/{searchTerm}/{pageNumber}/{itemsOnPage}")
    public Response getAllAccounts(@PathParam("searchTerm") String searchTerm, @PathParam("pageNumber") int pageNumber, @PathParam("itemsOnPage") int itemsOnPage) {
        PageResult<Account> result = null;
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
        Account result = null;
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
        Account result = null;

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


}
