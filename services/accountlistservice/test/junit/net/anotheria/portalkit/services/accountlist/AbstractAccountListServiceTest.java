package net.anotheria.portalkit.services.accountlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;
import net.anotheria.anoprise.eventservice.util.QueuedEventReceiver;
import net.anotheria.portalkit.services.accountlist.events.AccountListEvent;
import net.anotheria.portalkit.services.accountlist.events.AccountListServiceEventAnnouncer;
import net.anotheria.portalkit.services.common.AccountId;

import org.junit.Test;

/**
 * AbstractAccountListServiceTest.
 * 
 * @author dagafonov
 * 
 */
public abstract class AbstractAccountListServiceTest {

	/**
	 * Destination service instance.
	 */
	private AccountListService service;

	public void setService(AccountListService service) {
		this.service = service;
	}

	@Test
	public void testGetListAlwaysEmpty() throws AccountListServiceException {
		AccountId accId = AccountId.generateNew();

		List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "visited");
		// System.out.println("got " + accIdList.size() + " records...");
		assertNotNull(accIdList);
		assertTrue(accIdList.isEmpty());

	}

	private void checkCreationTimeStamp(List<AccountIdAdditionalInfo> accIdList) {
		for (AccountIdAdditionalInfo info : accIdList) {
			assertTrue("creation timestamp is empty", info.getCreationTimestamp() != 0);
		}
	}

	@Test
	public void testAddTargetToList() throws AccountListServiceException {
		AccountId accId = AccountId.generateNew();

		boolean res = service.addToList(accId, "contacts", new AccountIdAdditionalInfo(AccountId.generateNew(), "first"),
				new AccountIdAdditionalInfo(AccountId.generateNew(), "second"), new AccountIdAdditionalInfo(AccountId.generateNew(), "third"));
		assertTrue(res);

		List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "contacts");
		// System.out.println(accIdList);
		checkCreationTimeStamp(accIdList);

		assertNotNull(accIdList);
		assertEquals(3, accIdList.size());

	}

	@Test
	public void testUpdateTargetToList() throws AccountListServiceException {
		AccountId accId = AccountId.generateNew();

		AccountIdAdditionalInfo before = new AccountIdAdditionalInfo(AccountId.generateNew(), "before");

		service.updateInList(accId, "visits",
				Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(AccountId.generateNew()), before }));

		List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "visits");
		checkCreationTimeStamp(accIdList);

		assertNotNull(accIdList);
		assertEquals(2, accIdList.size());

		before.setCreationTimestamp(100L);

		boolean res = service.updateInList(
				accId,
				"visits",
				Arrays.asList(new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(AccountId.generateNew()),
						new AccountIdAdditionalInfo(AccountId.generateNew(), "second"),
						new AccountIdAdditionalInfo(AccountId.generateNew(), "third"), before }));

		assertTrue(res);

		accIdList = service.getList(accId, "visits");
		System.out.println(accIdList);
		checkCreationTimeStamp(accIdList);

		assertNotNull(accIdList);
		assertEquals(5, accIdList.size());

		for (AccountIdAdditionalInfo info : accIdList) {
			if (info.equals(before)) {
				System.out.println("--------------");
				assertEquals(100, info.getCreationTimestamp());
			}
		}
	}

	@Test
	public void testRemoveTargetFromList() throws AccountListServiceException {
		AccountId accId = AccountId.generateNew();

		AccountIdAdditionalInfo[] accList = new AccountIdAdditionalInfo[] { new AccountIdAdditionalInfo(AccountId.generateNew(), "first"),
				new AccountIdAdditionalInfo(AccountId.generateNew(), "second") };

		boolean res = service.addToList(accId, "favourites", new AccountIdAdditionalInfo(AccountId.generateNew(), "third"), accList);
		assertTrue(res);

		List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "favourites");
		checkCreationTimeStamp(accIdList);
		assertNotNull(accIdList);
		assertEquals(3, accIdList.size());

		res = service.removeFromList(accId, "favourites", Arrays.asList(accList));
		assertTrue(res);

		accIdList = service.getList(accId, "favourites");
		checkCreationTimeStamp(accIdList);
		assertNotNull(accIdList);
		assertEquals(1, accIdList.size());
	}

	@Test
	public void testReverseList() throws AccountListServiceException {
		AccountId accId = AccountId.generateNew();

		AccountId reverseAccId = AccountId.generateNew();

		boolean res = service.addToList(accId, "visits", new AccountIdAdditionalInfo(AccountId.generateNew(), "first"), new AccountIdAdditionalInfo(
				AccountId.generateNew(), "second"), new AccountIdAdditionalInfo(AccountId.generateNew(), "third"), new AccountIdAdditionalInfo(
				reverseAccId, ""));
		assertTrue(res);

		AccountId accId2 = AccountId.generateNew();
		res = service.addToList(accId2, "visits", new AccountIdAdditionalInfo(AccountId.generateNew(), "first"), new AccountIdAdditionalInfo(
				AccountId.generateNew(), "second"), new AccountIdAdditionalInfo(AccountId.generateNew(), "third"), new AccountIdAdditionalInfo(
				reverseAccId, ""));
		assertTrue(res);

		List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "visits");
		checkCreationTimeStamp(accIdList);
		assertNotNull(accIdList);
		assertEquals(4, accIdList.size());

		accIdList = service.reverseLookup(reverseAccId, "visits");
		checkCreationTimeStamp(accIdList);
		assertNotNull(accIdList);
		assertEquals(2, accIdList.size());

	}

	@Test
	public void testAnnouncer() throws AccountListServiceException {

		final AccountId accId = AccountId.generateNew();
		final CountDownLatch cdl = new CountDownLatch(1);

		EventServicePushConsumer cacheConsumer = new EventServicePushConsumer() {
			@Override
			public void push(Event event) {
				if (event.getData() instanceof AccountListEvent) {
					AccountListEvent accountListEvent = (AccountListEvent) event.getData();
					if (accId.equals(accountListEvent.getOwnerId())) {
						cdl.countDown();
					}
				}

			}
		};

		QueuedEventReceiver accountListEventReceiver = new QueuedEventReceiver("AccountListServiceEventReceiver",
				AccountListServiceEventAnnouncer.EVENT_CHANNEL_NAME, cacheConsumer);

		EventChannel channel = EventServiceFactory.createEventService().obtainEventChannel(AccountListServiceEventAnnouncer.EVENT_CHANNEL_NAME,
				accountListEventReceiver);
		channel.addConsumer(cacheConsumer);

		accountListEventReceiver.start();

		service.addToList(accId, "announcer", new AccountIdAdditionalInfo(AccountId.generateNew(), "first"));

		try {
			cdl.await();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

}
