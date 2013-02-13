package net.anotheria.portalkit.services.accountlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.anotheria.portalkit.services.accountlist.AccountListService;
import net.anotheria.portalkit.services.common.AccountId;

import org.junit.Test;

public abstract class AbstractAccountListServiceTest {

	private AccountListService service;

	public void setService(AccountListService service) {
		this.service = service;
	}

	@Test
	public void testGetListAlwaysEmpty() throws Exception {

		AccountId accId = AccountId.generateNew();

		List<AccountId> accIdList = service.getList(accId, "visited");
//		System.out.println("got " + accIdList.size() + " records...");
		assertNotNull(accIdList);
		assertTrue(accIdList.isEmpty());
	}

	@Test
	public void testAddTargetToList() throws Exception {

		AccountId accId = AccountId.generateNew();

		boolean res = service.addToList(accId, "contacts", AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
//		System.out.println("got " + res + " after insert...");
		assertTrue(res);

		List<AccountId> accIdList = service.getList(accId, "contacts");
//		System.out.println("got " + accIdList.size() + " records after insert...");
		assertNotNull(accIdList);
		assertEquals(3, accIdList.size());

	}

	@Test
	public void testRemoveTargetFromList() throws Exception {

		AccountId accId = AccountId.generateNew();

		AccountId[] accList = new AccountId[] { AccountId.generateNew(), AccountId.generateNew() };

		boolean res = service.addToList(accId, "favourites", AccountId.generateNew(), accList);
		assertTrue(res);

		List<AccountId> accIdList = service.getList(accId, "favourites");
//		System.out.println("got " + accIdList.size() + " records after insert...");
		assertNotNull(accIdList);
		assertEquals(3, accIdList.size());

		res = service.removeFromList(accId, "favourites", Arrays.asList(accList));
//		System.out.println("got " + res + " after removing...");
		assertTrue(res);

		accIdList = service.getList(accId, "favourites");
//		System.out.println("got " + accIdList.size() + " records after remove...");
		assertNotNull(accIdList);
		assertEquals(1, accIdList.size());

	}

	@Test
	public void testReverseList() throws Exception {

		AccountId accId = AccountId.generateNew();

		AccountId reverseAccId = AccountId.generateNew();

		boolean res = service.addToList(accId, "visits", AccountId.generateNew(), AccountId.generateNew(), reverseAccId, AccountId.generateNew());
		assertTrue(res);

		AccountId accId2 = AccountId.generateNew();
		res = service.addToList(accId2, "visits", AccountId.generateNew(), AccountId.generateNew(), reverseAccId, AccountId.generateNew());
		assertTrue(res);

		List<AccountId> accIdList = service.getList(accId, "visits");
		assertNotNull(accIdList);
		assertEquals(4, accIdList.size());

		accIdList = service.reverseLookup(reverseAccId, "visits");
		assertNotNull(accIdList);
		assertEquals(2, accIdList.size());

	}

}
