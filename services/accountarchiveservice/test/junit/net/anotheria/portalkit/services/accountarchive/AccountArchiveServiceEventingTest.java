package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.account.AccountServiceFactory;
import net.anotheria.portalkit.services.accountarchive.event.AccountArchiveServiceEventConsumer;
import net.anotheria.portalkit.services.accountarchive.event.data.ArchivedAccountEventData;
import net.anotheria.portalkit.services.accountarchive.event.data.ArchivedAccountUpdateEventData;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import net.anotheria.util.TimeUnit;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author VKoulakov
 * @since 24.04.14 17:04
 */
@Ignore
public class AccountArchiveServiceEventingTest {

    private static final int DAYS_AGO = 7;
//    private static final String PSQL = "psql";
    private AccountArchiveService accountArchiveService;
    private TestEventConsumer eventConsumer;
    private int accountCreateCount = 0;
    private int accountUpdateCount = 0;
    private int accountDeleteCount = 0;

    @Before
    public void init() {
        MetaFactory.reset();
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
        MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

        MetaFactory.addFactoryClass(AccountService.class, Extension.LOCAL, AccountServiceFactory.class);
        MetaFactory.addAlias(AccountService.class, Extension.LOCAL);

        try {
            accountArchiveService = MetaFactory.get(AccountArchiveService.class);
        } catch (MetaFactoryException e) {
            throw new RuntimeException(e);
        }

        eventConsumer = new TestEventConsumer();
    }

    @Test
    public void testCreatedEvent() throws AccountArchiveServiceException {
        assertThat(accountCreateCount, equalTo(0));
        assertThat(eventConsumer.getLastEventData(), nullValue());

        ArchivedAccount account = createAccountTemplate();
        ArchivedAccount created = accountArchiveService.createAccount(account);
        assertThat(accountCreateCount, equalTo(1));
        assertThat(eventConsumer.getLastEventData(), notNullValue());
        assertThat(created, not(sameInstance(account)));
        assertThat(created, equalTo(((ArchivedAccountEventData) eventConsumer.getLastEventData()).getAccount()));
    }

    @Test
    public void testUpdatedEvent() throws Exception {
        assertThat(accountUpdateCount, equalTo(0));
        assertThat(eventConsumer.getLastEventData(), nullValue());

        ArchivedAccount account = createAccountTemplate();
        ArchivedAccount created = accountArchiveService.createAccount(account);
        created.setName("updated");
        accountArchiveService.updateAccount(account);
        assertThat(accountUpdateCount, equalTo(1));
        assertThat(eventConsumer.getLastEventData(), notNullValue());
        ArchivedAccount updated = accountArchiveService.getAccount(created.getId());
        assertThat(updated, notNullValue());
        assertThat(account, equalTo(((ArchivedAccountUpdateEventData) eventConsumer.getLastEventData()).getBeforeUpdate()));
        assertThat(updated, equalTo(((ArchivedAccountUpdateEventData) eventConsumer.getLastEventData()).getAfterUpdate()));
    }

    @Test
    public void testDeletedEvent() throws Exception {
        assertThat(accountDeleteCount, equalTo(0));
        assertThat(eventConsumer.getLastEventData(), nullValue());

        ArchivedAccount account = createAccountTemplate();
        accountArchiveService.createAccount(account);
        accountArchiveService.deleteAccount(account.getId());

        assertThat(accountDeleteCount, equalTo(1));
        assertThat(eventConsumer.getLastEventData(), notNullValue());
        ArchivedAccount deleted = ((ArchivedAccountEventData)eventConsumer.getLastEventData()).getAccount();
        assertThat(account, not(sameInstance(deleted)));
        assertThat(account, equalTo(deleted));
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

    class TestEventConsumer extends AccountArchiveServiceEventConsumer {

        private ServiceEventData lastEventData;

        ServiceEventData getLastEventData() {
            return lastEventData;
        }

        @Override
        protected void archivedAccountCreated(ArchivedAccountEventData eventData) {
            accountCreateCount++;
            lastEventData = eventData;
        }

        @Override
        protected void archivedAccountUpdated(ArchivedAccountUpdateEventData eventData) {
            accountUpdateCount++;
            lastEventData = eventData;
        }

        @Override
        protected void archivedAccountDeleted(ArchivedAccountEventData eventData) {
            accountDeleteCount++;
            lastEventData = eventData;
        }
    }

}
