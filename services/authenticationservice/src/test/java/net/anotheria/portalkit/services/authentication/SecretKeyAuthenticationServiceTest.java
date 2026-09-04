package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.authentication.persistence.inmemory.InMemoryAuthenticationPersistenceServiceFactory;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;

import java.util.List;
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

        EncryptedAuthToken oldEncrypted = service.generateEncryptedToken(dummyAccountId, oldToken);

        AuthToken newToken = new AuthToken();
        newToken.setType(1);
        newToken.setAccountId(dummyAccountId);
        newToken.setExclusiveInType(true);

        EncryptedAuthToken newEncrypted = service.generateEncryptedToken(dummyAccountId, newToken);

        assertFalse(persistenceService.authTokenExists(oldEncrypted.getEncryptedVersion()));
        assertTrue(persistenceService.authTokenExists(newEncrypted.getEncryptedVersion()));
    }

    @Test
    public void tokenInventoryReportsRealAccountIds() throws MetaFactoryException, AuthenticationServiceException {
        SecretKeyAuthenticationService service = MetaFactory.get(SecretKeyAuthenticationService.class);

        AccountId accountId = AccountId.generateNew();
        AuthToken token = new AuthToken();
        token.setType(15);
        token.setMultiUse(true);
        token.setAccountId(accountId);
        token.setExpiryTimestamp(System.currentTimeMillis() + 60000L);

        service.generateEncryptedToken(accountId, token);

        //this implementation stores the account id encrypted. An inventory entry carrying that encrypted id
        //would be useless - the caller wants to look the account up to see who owns or created the token.
        List<TokenInventoryEntry> byAccount = service.getTokenInventoryByAccount(accountId);
        assertEquals(1, byAccount.size());
        assertEquals(accountId, byAccount.get(0).getAccountId());

        List<TokenInventoryEntry> byType = service.getTokenInventoryByType(15, 100, 0);
        assertEquals(1, byType.size());
        assertEquals("the type scoped inventory has to report the real account id too", accountId, byType.get(0).getAccountId());
    }

}
