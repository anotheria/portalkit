package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;

/**
 * API to perform authentication for admin-API.
 */
public interface AdminAuthAPI extends API {

    /**
     * Authenticates user by token. If token is valid, returns login and allows the further processing, otherwise throws an exception.
     *
     * @param authToken token to verify
     * @return login in case of valid token
     * @throws AdminAPIAuthenticationException in case of invalid token
     */
    String authenticateByToken(String authToken) throws AdminAPIAuthenticationException;

    /**
     * Used to initially log in user in admin-API. Verifies credentials and if there are valid, returns a new created authToken. If not - exception.
     * AuthToken then is used to call secured endpoints.
     *
     * @param login    login
     * @param password password
     * @return token in case of valid credentials
     * @throws AdminAPIAuthenticationException in case of invalid credentials
     */
    String login(String login, String password) throws AdminAPIAuthenticationException;

    /**
     * Logouts user from admin-API.
     * Deletes current authToken.
     */
    void logout();

}
