package net.anotheria.portalkit.services.authentication;

import org.distributeme.core.ServiceLocator;

public class TestForCasedrive {
    public static void main(String[] a) throws Exception{
        SecretKeyAuthenticationService service = ServiceLocator.getRemote(SecretKeyAuthenticationService.class);
        service.authenticateByEncryptedToken("TOKEN");
    }
}
