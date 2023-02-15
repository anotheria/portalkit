package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.authentication.persistence.inmemory.InMemoryAuthenticationPersistenceServiceFactory;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecretKeyAuthenticationServiceTest {

    @Before
    @After
    public void setup() {
        MetaFactory.reset();
        MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

        MetaFactory.addFactoryClass(SecretKeyAuthenticationService.class, Extension.LOCAL, SecretKeyAuthenticationServiceFactory.class);
        MetaFactory.addAlias(SecretKeyAuthenticationService.class, Extension.LOCAL);

        MetaFactory.addFactoryClass(AuthenticationPersistenceService.class, Extension.LOCAL, InMemoryAuthenticationPersistenceServiceFactory.class);
        MetaFactory.addAlias(AuthenticationPersistenceService.class, Extension.LOCAL);
    }

    @Test
    public void testGenerateAuthToken() throws MetaFactoryException, AuthenticationServiceException, AuthenticationPersistenceServiceException {
        AuthenticationPersistenceService persistenceService = MetaFactory.get(AuthenticationPersistenceService.class);
        SecretKeyAuthenticationService service = MetaFactory.get(SecretKeyAuthenticationService.class);

        AccountId dummyAccountId = AccountId.generateNew();
        AuthToken oldToken = new AuthToken();
        oldToken.setType(1);
        oldToken.setAccountId(dummyAccountId);
        oldToken.setExclusiveInType(true);
        oldToken.setMultiUse(false);
        oldToken.setExclusive(false);

        EncryptedAuthToken oldEncrypted = service.generateEncryptedToken(dummyAccountId, oldToken);

        AuthToken newToken = new AuthToken();
        newToken.setType(1);
        newToken.setAccountId(dummyAccountId);
        newToken.setExclusiveInType(true);
        newToken.setMultiUse(false);
        newToken.setExclusive(false);

        EncryptedAuthToken newEncrypted = service.generateEncryptedToken(dummyAccountId, newToken);

        assertFalse(persistenceService.authTokenExists(oldEncrypted.getEncryptedVersion()));
        assertTrue(persistenceService.authTokenExists(newEncrypted.getEncryptedVersion()));
    }

}
