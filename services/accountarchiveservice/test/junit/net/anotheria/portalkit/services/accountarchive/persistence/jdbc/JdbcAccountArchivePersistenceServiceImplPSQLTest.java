package net.anotheria.portalkit.services.accountarchive.persistence.jdbc;

import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.persistence.ArchivedAccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.TimeUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author VKoulakov
 * @since 24.04.14 12:39
 */
public class JdbcAccountArchivePersistenceServiceImplPSQLTest extends JdbcAccountArchivePersistenceServiceImplTest {

    public static final int DAYS_AGO = 7;
    public static final String PSQL = "psql";
    public static final int MODIFIED_TYPE = 100;
    private JdbcAccountArchivePersistenceServiceImpl service;
    private ArchivedAccount created;

    @Before
    public void init() throws ArchivedAccountPersistenceServiceException {
        service = getService(PSQL);
        created = createAccountTemplate();
        service.saveAccount(created);
    }

    @Test
    public void testCreateArchivedAccount() throws ArchivedAccountPersistenceServiceException {
        Assert.assertNotNull(created.getId());
        ArchivedAccount result = service.getAccount(created.getId());
        assertThat(result, equalTo(created));
    }

    @Test
    public void testGetArchivedAccount() throws ArchivedAccountPersistenceServiceException {
        AccountId any = AccountId.generateNew();
        ArchivedAccount notFound = service.getAccount(any);
        assertThat(notFound, nullValue());

        ArchivedAccount existing = service.getAccount(created.getId());
        assertThat(created, not(sameInstance(existing)));
        assertThat(existing, equalTo(existing));
    }

    @Test
    public void testGetListOfArchivedAccounts() throws ArchivedAccountPersistenceServiceException {
        List<AccountId> identities = new ArrayList<AccountId>();
        for (int i = 0; i < 5; i++) {
            ArchivedAccount toCreate = createAccountTemplate();
            service.saveAccount(toCreate);
            identities.add(toCreate.getId());
        }

        List<ArchivedAccount> loaded = service.getAccounts(identities);
        assertThat(loaded, notNullValue());
        assertThat("not completed list of accounts was loaded", identities.size(), equalTo(loaded.size()));
        assertThat(identities.contains(loaded.get(2).getId()), is(true));

        identities.add(new AccountId("invisible")); //non-existing element
        loaded = service.getAccounts(identities);
        assertThat(loaded, notNullValue());
        assertThat(loaded.size(), equalTo(identities.size() - 1));

        identities.clear();
        identities.add(new AccountId("1"));
        identities.add(new AccountId("2"));
        loaded = service.getAccounts(identities);
        assertThat(loaded, notNullValue());
        assertThat(loaded.isEmpty(), is(true));
    }

    @Test @Ignore
    public void testDeleteArchivedAccount() throws ArchivedAccountPersistenceServiceException {
        AccountId any = AccountId.generateNew();
        service.deleteAccount(any);

        ArchivedAccount existing = service.getAccount(created.getId());
        assertThat(created.getId(), equalTo(existing.getId()));

        service.deleteAccount(created.getId());

        ArchivedAccount notFound = service.getAccount(created.getId());
        assertThat(notFound, nullValue());
    }

    @Test
    public void testEditArchivedAccount() throws ArchivedAccountPersistenceServiceException {
        ArchivedAccount existing = service.getAccount(created.getId());
        assertThat(created.getId(), equalTo(existing.getId()));

        existing.setType(MODIFIED_TYPE);
        service.saveAccount(existing);

        ArchivedAccount reloaded = service.getAccount(existing.getId());
        assertThat(reloaded.getId(), equalTo(existing.getId()));
        assertThat(reloaded.getType(), equalTo(MODIFIED_TYPE));
    }

    @Test
    public void testArchivedAccountFields() throws ArchivedAccountPersistenceServiceException {
        ArchivedAccount loaded = service.getAccount(created.getId());
        assertThat(created, equalTo(loaded));
        assertThat(created, not(sameInstance(loaded)));
        assertThat("name mismatch", created.getName(), equalTo(loaded.getName()));
        assertThat("type mismatch", created.getType(), equalTo(loaded.getType()));
        assertThat("email mismatch", created.getEmail(), equalTo(loaded.getEmail()));
        assertThat("status mismatch", created.getStatus(), equalTo(loaded.getStatus()));
        assertThat("registartion time mismatch", created.getRegistrationTimestamp(), equalTo(loaded.getRegistrationTimestamp()));
        assertThat("deletion time mismatch", created.getDeletionTimestamp(), equalTo(loaded.getDeletionTimestamp()));
        assertThat("deletion note mismatch", created.getDeletionNote(), equalTo(loaded.getDeletionNote()));
    }

    @Test
    public void testGetByEmail() throws ArchivedAccountPersistenceServiceException {
        ArchivedAccount toCreate1 = createAccountTemplate();
        toCreate1.setEmail("test1@anotheria.net");
        ArchivedAccount toCreate2 = createAccountTemplate();
        toCreate2.setEmail("test2@anotheria.net");
        service.saveAccount(toCreate1);
        service.saveAccount(toCreate2);

        assertThat(service.getIdByEmail("unknown@email.com"), nullValue());
        AccountId id1 = service.getIdByEmail("test1@anotheria.net");
        assertThat(id1, equalTo(toCreate1.getId()));
        assertThat(service.getIdByEmail("test2@anotheria.net"), equalTo(toCreate2.getId()));
        assertThat(service.getAccount(id1).getEmail(), equalTo(toCreate1.getEmail()));
    }

    @Test
    public void testGetByName() throws ArchivedAccountPersistenceServiceException {
        ArchivedAccount toCreate1 = createAccountTemplate();
        toCreate1.setName("test1");
        ArchivedAccount toCreate2 = createAccountTemplate();
        toCreate2.setName("test2");
        service.saveAccount(toCreate1);
        service.saveAccount(toCreate2);

        assertThat(service.getIdByName("unknown"), nullValue());
        AccountId id1 = service.getIdByName("test1");
        assertThat(id1, equalTo(toCreate1.getId()));
        assertThat(service.getIdByName("test2"), equalTo(toCreate2.getId()));
        assertThat(service.getAccount(id1).getName(), equalTo(toCreate1.getName()));
    }

    private ArchivedAccount createAccountTemplate() {
        ArchivedAccount account = new ArchivedAccount(AccountId.generateNew());
        account.setName("test");
        account.setEmail("test@example.com");
        account.setType(1);
        account.setRegistrationTimestamp(System.currentTimeMillis());
        account.addStatus(1);
        account.addStatus(16);
        account.addStatus(32);
        long now = System.currentTimeMillis();
        account.setDeletionTimestamp(now - TimeUnit.DAY.getMillis(DAYS_AGO));
        account.setDeletionNote("test deletion");
        return account;
    }
}
