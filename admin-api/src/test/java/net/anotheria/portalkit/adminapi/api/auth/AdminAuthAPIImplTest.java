package net.anotheria.portalkit.adminapi.api.auth;

import net.anotheria.portalkit.adminapi.api.auth.provider.AuthProvider;
import net.anotheria.util.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminAuthAPIImplTest {

    @Mock
    private AuthProvider authProvider;

    private List<AuthTokenAO> tokens = new LinkedList<>();

    @InjectMocks
    private AdminAuthAPIImpl testAdminAuthImpl = new AdminAuthAPIImpl();

    @Before
    public void setUp() throws Exception {
        testAdminAuthImpl.setTokens(tokens);
    }

    @Test
    public void testAuthenticateByToken() throws AdminAPIAuthenticationException {
        tokens.clear();

        // given
        String authToken = "token";

        AuthTokenAO token = new AuthTokenAO();
        token.setToken(authToken);
        token.setLogin("login");
        token.setExpirationTime(TimeUnit.HOUR.getMillis(2));
        token.setTimestamp(System.currentTimeMillis());
        tokens.add(token);

        // when
        String result = testAdminAuthImpl.authenticateByToken(authToken);

        // then
        assertEquals(token.getLogin(), result);
    }

    @Test
    public void testAuthenticateByTokenExpired() {
        tokens.clear();

        // given
        String authToken = "token";

        AuthTokenAO token = new AuthTokenAO();
        token.setToken(authToken);
        token.setLogin("login");
        token.setExpirationTime(TimeUnit.HOUR.getMillis(1));
        token.setTimestamp(System.currentTimeMillis() - TimeUnit.HOUR.getMillis(2));
        tokens.add(token);

        try {

            // when
            testAdminAuthImpl.authenticateByToken(authToken);

            // then
            fail("exception failed");
        } catch (AdminAPIAuthenticationException ignored) {
        }
    }

    @Test
    public void testAuthenticateByTokenNoToken() {
        tokens.clear();

        // given
        String authToken = "token";

        try {

            // when
            testAdminAuthImpl.authenticateByToken(authToken);

            // then
            fail("exception failed");
        } catch (AdminAPIAuthenticationException ignored) {
        }
    }

    @Test
    public void testLogin() throws AdminAPIAuthenticationException, AdminAuthenticationProviderException {
        tokens.clear();

        // given
        String login = "login";
        String password = "password";

        doNothing().when(authProvider).authenticate(login, password);

        // when
        String result = testAdminAuthImpl.login(login, password);

        // then
        assertTrue(result.contains("B:") && result.length() == 202);
        assertEquals(1, tokens.size());
        assertEquals(result, tokens.get(0).getToken());
        assertEquals(login, tokens.get(0).getLogin());
    }

    @Test
    public void testLoginProviderError() throws AdminAuthenticationProviderException {
        tokens.clear();

        // given
        String login = "login";
        String password = "password";

        doThrow(new AdminAuthenticationProviderException()).when(authProvider).authenticate(login, password);

        try {

            // when
            testAdminAuthImpl.login(login, password);

            // then
            fail("exception expected");
        } catch (AdminAPIAuthenticationException ignored) {
        }
    }

}
