package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.portalkit.adminapi.api.auth.AdminAuthenticationProviderException;
import net.anotheria.portalkit.adminapi.config.AuthenticationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ConfigAuthProviderTest {

    @Mock
    private AuthenticationConfig config;

    @InjectMocks
    private ConfigAuthProvider testAuthProvider = new ConfigAuthProvider();

    @Test
    public void testAuthenticate() throws AdminAuthenticationProviderException {

        // given
        String login = "login";
        String password = "password";

        AuthenticationConfig.AccountConfig accountConfig = new AuthenticationConfig.AccountConfig();
        accountConfig.setLogin(login);
        accountConfig.setPassword(password);
        given(config.getAccountByLogin(login)).willReturn(accountConfig);

        // when
        testAuthProvider.authenticate(login, password);
    }

    @Test
    public void testAuthenticateAccountDoesntExists() {

        // given
        String login = "login";
        String password = "password";

        given(config.getAccountByLogin(login)).willReturn(null);

        try {

            // when
            testAuthProvider.authenticate(login, password);

            // then
            fail("exception expected");
        } catch (AdminAuthenticationProviderException ignored) {
        }
    }

    @Test
    public void testAuthenticateAccountInvalidPassword() {

        // given
        String login = "login";
        String password = "password";

        AuthenticationConfig.AccountConfig accountConfig = new AuthenticationConfig.AccountConfig();
        accountConfig.setLogin(login);
        accountConfig.setPassword("qweqwe");
        given(config.getAccountByLogin(login)).willReturn(accountConfig);

        try {

            // when
            testAuthProvider.authenticate(login, password);

            // then
            fail("exception expected");
        } catch (AdminAuthenticationProviderException ignored) {
        }
    }

    @Test
    public void testGetProviderType() {
        assertEquals(AuthProviderType.CONFIG, testAuthProvider.getProviderType());
    }

}
