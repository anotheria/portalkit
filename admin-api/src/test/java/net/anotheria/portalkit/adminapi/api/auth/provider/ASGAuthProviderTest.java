package net.anotheria.portalkit.adminapi.api.auth.provider;

import net.anotheria.anosite.cms.user.CMSUserManager;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthenticationProviderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ASGAuthProviderTest {

    @Mock
    private CMSUserManager userManager;

    @InjectMocks
    private ASGAuthProvider testAuthProvider = new ASGAuthProvider(true);

    @Test
    public void testAuthenticate() throws AdminAuthenticationProviderException {

        // given
        String login = "login";
        String password = "password";
        given(userManager.canLoginUser(login, password)).willReturn(true);

        // when
        testAuthProvider.authenticate(login, password);
    }

    @Test
    public void testAuthenticateBadCredentials() {

        // given
        String login = "login";
        String password = "password";
        given(userManager.canLoginUser(login, password)).willReturn(false);

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
        assertEquals(AuthProviderType.ASG, testAuthProvider.getProviderType());
    }

}
