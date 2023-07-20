package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.portalkit.adminapi.api.auth.provider.AuthProvider;
import net.anotheria.portalkit.adminapi.api.auth.provider.AuthProviderFactory;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Vector;

public class AdminAuthAPIImpl extends AbstractAPIImpl implements AdminAuthAPI {

    private static final int TOKEN_EXPIRATION_TIME = (int) java.util.concurrent.TimeUnit.HOURS.toMillis(1);

    private List<AuthTokenAO> tokens = new Vector<>();
    private AuthProvider authProvider;

    @Override
    public void init() throws APIInitException {
        super.init();

        this.authProvider = AuthProviderFactory.getAuthProvider();
    }

    @Override
    public String authenticateByToken(String authToken) throws AdminAPIAuthenticationException {

        // check if there is an account bound with a provided token
        String account = null;
        for (AuthTokenAO token : tokens) {
            if (token.getToken().equals(authToken) && (System.currentTimeMillis() - token.getTimestamp() < token.getExpirationTime())) {
                account = token.getLogin();
            }
        }

        if (account == null) {
            throw new AdminAPIAuthenticationException("Authentication failed. Invalid or expired token");
        }

        return account;
    }

    @Override
    public String login(String login, String password) throws AdminAPIAuthenticationException {
        String result = null;
        try {
            authProvider.authenticate(login, password);

            result = generateAuthToken();
            tokens.add(new AuthTokenAO(result, login, TOKEN_EXPIRATION_TIME, System.currentTimeMillis()));
        } catch (AdminAuthenticationProviderException ex) {
            throw new AdminAPIAuthenticationException(AdminAPIAuthenticationException.FailCause.PASSWORD_DOESNT_MATCH);
        }
        return result;
    }

    @Override
    public void logout() {
        String currentToken = (String) APICallContext.getCallContext().getAttribute("AUTH_TOKEN");
        tokens.removeIf(e -> e.getToken().equals(currentToken));
    }

    private String generateAuthToken() {
        return "B:".concat(RandomStringUtils.randomAlphanumeric(200));
    }
}
