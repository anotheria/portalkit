package net.anotheria.portalkit.apis.common.authentication;


import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.account.AccountNotFoundException;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PortalKitAuthenticationAPIImplManualLoginTest {


    public static final AccountId ACCOUNT_ID = AccountId.generateNew();
    @Mock
    AccountService accountService;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    PortalKitAuthenticationAPIImpl portalKitAuthenticationAPI;

    @Test
    public void testManualLoginNonExistingUser() throws Exception{

        when(accountService.getAccountIdByEmail("non-existing")).thenThrow(new AccountNotFoundException("non-existing"));

        try {
            portalKitAuthenticationAPI.manualLogin("non-existing", "bla");
            Assert.fail("Should throw an exception");
        }catch(net.anotheria.portalkit.apis.common.authentication.AccountNotFoundException e){
            //success
        }
    }

    @Test
    public void testManualLoginExistingUser() throws Exception{

        when(accountService.getAccountIdByEmail("user")).thenReturn(ACCOUNT_ID);
        when(authenticationService.canAuthenticate(ACCOUNT_ID, "password")).thenReturn(true);

        try {
            portalKitAuthenticationAPI.manualLogin("user", "password");
        }catch(net.anotheria.portalkit.apis.common.authentication.AccountNotFoundException e){
            //success
        }
    }

    @Test
    public void testManualLoginExistingUserWrongPassword() throws Exception{

        when(accountService.getAccountIdByEmail("user")).thenReturn(ACCOUNT_ID);

        try {
            portalKitAuthenticationAPI.manualLogin("user", "xxxxxx");
            Assert.fail("Except an exception");
        }catch(net.anotheria.portalkit.apis.common.authentication.IllegalCredentialsForLoginException e){
            //success
        }
    }
}
