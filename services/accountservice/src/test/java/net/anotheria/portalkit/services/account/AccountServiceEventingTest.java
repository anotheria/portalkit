package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.account.event.AccountServiceEventConsumer;
import net.anotheria.portalkit.services.account.event.data.AccountCreateEventData;
import net.anotheria.portalkit.services.account.event.data.AccountDeleteEventData;
import net.anotheria.portalkit.services.account.event.data.AccountUpdateEventData;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceService;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

/**
 * {@link AccountService} eventing test.
 *
 * @author Alex Osadchy
 */
public final class AccountServiceEventingTest {

	/**
	 * {@link AccountServiceTestEventConsumer}.
	 */
	private AccountServiceTestEventConsumer eventConsumer;
	// counters
	private int accountCreateCount = 0;
	private int accountUpdateCount = 0;
	private int accountDeleteCount = 0;

	/**
	 * {@link AccountService} instance.
	 */
	private AccountServiceImpl accountService = null;

	@Before
	public void init() {
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

		MetaFactory.createOnTheFlyFactory(AccountAuditPersistenceService.class, Extension.NONE, new AccountAuditPersistenceService() {
			@Override
			public void saveAccountAudit(AccountAudit accountAudit) throws AccountAuditPersistenceServiceException {

			}

			@Override
			public List<AccountAudit> getAccountAudits(AccountId accountId) throws AccountAuditPersistenceServiceException {
				return null;
			}
		});

		accountService = AccountServiceImpl.INSTANCE;
		accountService.unitTestReset();
		eventConsumer = new AccountServiceTestEventConsumer();
	}

	@Test
	public void createAccountEventTest() throws AccountServiceException {
		assertEquals(0, accountCreateCount);
		assertNull(eventConsumer.getLastReceivedEventData());

		Account account = new Account(new AccountId("test_account_1"));
		account.setEmail("admin@portalkit.com");
		account.setName("admin");

		Account created = accountService.createAccount(account);

		assertEquals(1, accountCreateCount);

		assertNotNull(eventConsumer.getLastReceivedEventData());
		assertNotSame(created, AccountCreateEventData.class.cast(eventConsumer.getLastReceivedEventData()).getAccount());
		assertEquals(created, AccountCreateEventData.class.cast(eventConsumer.getLastReceivedEventData()).getAccount());
	}

	@Test
	public void updateAccountEventTest() throws AccountServiceException {
		assertEquals(0, accountUpdateCount);
		assertNull(eventConsumer.getLastReceivedEventData());

		Account account = new Account(new AccountId("test_account_1"));
		account.setEmail("admin@portalkit.com");
		account.setName("admin");
		Account created = accountService.createAccount(account);

		assertEquals(0, accountUpdateCount);

		created.setName("name_updated");
		accountService.updateAccount(created);

		assertEquals(1, accountUpdateCount);
		assertNotNull(eventConsumer.getLastReceivedEventData());
		assertEquals("admin", AccountUpdateEventData.class.cast(eventConsumer.getLastReceivedEventData()).getBeforeUpdate().getName());
		assertEquals("name_updated", AccountUpdateEventData.class.cast(eventConsumer.getLastReceivedEventData()).getAfterUpdate().getName());
	}

	@Test
	public void deleteAccountEventTest() throws AccountServiceException {
		assertEquals(0, accountDeleteCount);
		assertNull(eventConsumer.getLastReceivedEventData());

		Account account = new Account(new AccountId("test_account_1"));
		account.setEmail("admin@portalkit.com");
		account.setName("admin");
		Account created = accountService.createAccount(account);

		assertEquals(0, accountDeleteCount);

		accountService.deleteAccount(created.getId());

		assertEquals(1, accountDeleteCount);
		assertNotNull(eventConsumer.getLastReceivedEventData());
		assertEquals(created, AccountDeleteEventData.class.cast(eventConsumer.getLastReceivedEventData()).getAccount());
	}

	/**
	 * Test {@link AccountServiceEventConsumer}.
	 */
	public class AccountServiceTestEventConsumer extends AccountServiceEventConsumer {

		/**
		 * Most recent {@link ServiceEventData}.
		 */
		private ServiceEventData lastReceivedEventData;

		@Override
		public void accountCreated(AccountCreateEventData eventData) {
			accountCreateCount++;
			lastReceivedEventData = eventData;
		}

		@Override
		public void accountUpdated(AccountUpdateEventData eventData) {
			accountUpdateCount++;
			lastReceivedEventData = eventData;

		}

		@Override
		public void accountDeleted(AccountDeleteEventData eventData) {
			accountDeleteCount++;
			lastReceivedEventData = eventData;
		}

		public ServiceEventData getLastReceivedEventData() {
			return lastReceivedEventData;
		}
	}
}
