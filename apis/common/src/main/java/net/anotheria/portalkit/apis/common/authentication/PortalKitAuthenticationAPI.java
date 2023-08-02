package net.anotheria.portalkit.apis.common.authentication;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Generic class for handling authentication with portalkit, provides default methods for handling of manual and direct login.
 */
public interface PortalKitAuthenticationAPI extends API {
    /**
     * Basic login functionality for login with username and password.
     * @param username
     * @param password
     * @return
     * @throws APIException
     */
    Account manualLogin(String username, String password) throws APIException;

    void setPassword(AccountId accountId, String password) throws APIException;
}
