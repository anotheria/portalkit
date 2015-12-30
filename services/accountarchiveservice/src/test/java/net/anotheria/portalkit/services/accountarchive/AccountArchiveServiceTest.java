package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.account.AccountServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author VKoulakov
 * @since 24.04.14 14:07
 */
@Ignore
public class AccountArchiveServiceTest {

//    public static final String PSQL = "psql";
    private AccountArchiveService service;

    @After
    @Before
    public void setup() throws MetaFactoryException {
        MetaFactory.reset();
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
        MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

        MetaFactory.addFactoryClass(AccountArchiveService.class, Extension.LOCAL, AccountArchiveServiceFactory.class);
        MetaFactory.addAlias(AccountArchiveService.class, Extension.LOCAL);

        service = MetaFactory.get(AccountArchiveService.class);
    }

    @Test
    public void testNotExistingAccount() throws MetaFactoryException, AccountServiceException {
        AccountId newAccountId = AccountId.generateNew();
        try {
            service.getAccount(newAccountId);
            fail("Exception expected");
        } catch (AccountArchiveServiceException ignored) {
        }
        try {
            service.deleteAccount(newAccountId);
            fail("Exception expected");
        } catch (AccountArchiveServiceException ignored) {
        }
    }

    @Test
    public void testCreateAccount() throws AccountArchiveServiceException {
        ArchivedAccount toCreate = AccountArchiveUtils.createAccountTemplate();
        ArchivedAccount created = service.createAccount(toCreate);
        assertThat(created, notNullValue());
        assertThat(created, not(sameInstance(toCreate)));
        assertThat(created, equalTo(toCreate));
    }

    @Test
    public void testUpdateAccount() throws AccountArchiveServiceException {
        ArchivedAccount toUpdate = AccountArchiveUtils.createAccountTemplate();
        ArchivedAccount beforeUpdate = new ArchivedAccount();
        beforeUpdate.copyFrom(toUpdate);
        service.createAccount(toUpdate);
        toUpdate.setDeletionNote("updated");
        service.updateAccount(toUpdate);
        ArchivedAccount fetched = service.getAccount(toUpdate.getId());
        assertThat(fetched, notNullValue());
        assertThat(fetched, not(sameInstance(toUpdate)));
        assertThat(fetched, equalTo(toUpdate));
        assertThat(fetched, not(equalTo(beforeUpdate)));
    }

    @Test @Ignore
    public void testDeleteAccount() throws AccountArchiveServiceException {
        ArchivedAccount toDelete = AccountArchiveUtils.createAccountTemplate();
        service.createAccount(toDelete);
        service.deleteAccount(toDelete.getId());
        try{
            service.getAccount(toDelete.getId());
            fail("ArchivedAccountNotFoundException expected");
        }catch(ArchivedAccountNotFoundException ignored){}
    }

}
