package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;

public interface AdminAuthAPI extends API {

    String authenticateByToken(String authToken) throws AdminAPIAuthenticationException;

    String login(String login, String password) throws AdminAPIAuthenticationException;

    void logout();

}
