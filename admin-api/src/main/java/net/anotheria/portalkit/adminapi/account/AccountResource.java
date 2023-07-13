package net.anotheria.portalkit.adminapi.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountAdminService;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.common.AccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("admin-api/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private static final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private AccountService accountService;
    private AccountAdminService accountAdminService;

    private Gson gson;
    private GsonBuilder builder;

    public AccountResource() {
        this.builder = new GsonBuilder();
        this.builder.setPrettyPrinting();
        this.gson = builder.create();

        try {
            this.accountService = MetaFactory.get(AccountService.class);
            this.accountAdminService = MetaFactory.get(AccountAdminService.class);
        } catch (MetaFactoryException ex) {
            log.error("Cannot initialize AccountResource", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @GET
    public Response getAllAccounts() {
        List<Account> result = null;
        try {
            List<AccountId> accountIds = new LinkedList<>(accountAdminService.getAllAccountIds());
            result = accountService.getAccounts(accountIds);
        } catch (Exception any) {
            log.error("Cannot get all accounts", any);
            return Response.status(500).build();
        }
        return Response.status(200).entity(gson.toJson(result)).build();
    }

}
