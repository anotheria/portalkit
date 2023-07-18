package net.anotheria.portalkit.adminapi.rest.filter.auth;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.portalkit.services.common.AccountId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * Authorization functional for {@link javax.servlet.Filter}.
 */
public class FilterAuthorization {

    private static final Logger log = LoggerFactory.getLogger(FilterAuthorization.class);

    private static final AuthResult NO_AUTH_TOKEN_RESULT = new AuthResult(new AuthError(HttpServletResponse.SC_UNAUTHORIZED, "No authToken"));

    public AuthResult authorize(String httpAuthString, String websocketProtocol, String websocketAuthToken, HttpServletResponse response, boolean isAdminTokenRequired) {
        String authString;
        if (httpAuthString != null) {
            authString = httpAuthString;
        } else {
            return NO_AUTH_TOKEN_RESULT;
        }

        AccountId accountId;

        APICallContext callContext = APICallContext.getCallContext();
        accountId = null;

        callContext.setCurrentUserId(accountId.getInternalId());
        callContext.setAttribute("authToken", authString);

        AccountId driveOwnerId = accountId;


        return new AuthResult(accountId);
    }
}
