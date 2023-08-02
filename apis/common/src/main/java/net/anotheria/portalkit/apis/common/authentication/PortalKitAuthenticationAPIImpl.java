package net.anotheria.portalkit.apis.common.authentication;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.apis.common.BasePortalKitAPIImpl;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountNotFoundException;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.account.AccountServiceException;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.authentication.AuthenticationServiceException;
import net.anotheria.portalkit.services.common.AccountId;

public class PortalKitAuthenticationAPIImpl extends BasePortalKitAPIImpl implements PortalKitAuthenticationAPI {

    private AccountService accountService;
    private AuthenticationService authenticationService;

    @Override
    public void init() throws APIInitException {
        super.init();

        try {
            accountService = createAccountService();
            authenticationService = createAuthenticationService();
        }catch(MetaFactoryException e){
            throw new APIInitException("Can't initialize services, probably a misconfiguration", e);
        }
    }

    @Override
    public Account manualLogin(String username, String password) throws APIException {

        //first lookup account, if account not found, an exception will be thrown.
        AccountId accountId = lookupAccountIdByLoginCredential(username);
        //check if the supplied credentials are valid.
        try {
            if (!authenticationService.canAuthenticate(accountId, password)) {
                throw new IllegalCredentialsForLoginException("Password mismatch");
            }
        }catch(AuthenticationServiceException e){
            throw new APIException("Can't lookup credentials", e);
        }

        //we past credentials, retrieve Account.
        Account account = null;
        try {
            account = accountService.getAccount(accountId);
        }catch(AccountServiceException e){
            throw new APIException("Can't retrieve account with id "+accountId, e);
        }

        return account;
    }

    @Override
    public void setPassword(AccountId accountId, String password) throws APIException {

    }


    /**
     * This method is separate to allow extending class to overwrite account lookup if required.
     * Default behaviour is by name field in AccountService.
     * @param username
     * @return
     */
    protected AccountId lookupAccountIdByLoginCredential(String username) throws APIException{
        AccountId accountId;
        try {
            accountId = accountService.getAccountIdByEmail(username);
        }catch(AccountNotFoundException e){
            throw new net.anotheria.portalkit.apis.common.authentication.AccountNotFoundException(username);
        }catch(AccountServiceException e){
            throw new APIException("lookupAccountByLoginCredential("+username+", backend failure", e);
        }
        return accountId;
    }

    /**
     * Creates the AccountService. A separate method to allow overloading.
     * @return
     * @throws MetaFactoryException
     */
    protected AccountService createAccountService() throws MetaFactoryException {
        return MetaFactory.get(AccountService.class);
    }

    /**
     * Creates the AuthenticationService. A separate method to allow overloading.
     * @return
     * @throws MetaFactoryException
     */
    protected AuthenticationService createAuthenticationService() throws MetaFactoryException {
        return MetaFactory.get(AuthenticationService.class);
    }

}
