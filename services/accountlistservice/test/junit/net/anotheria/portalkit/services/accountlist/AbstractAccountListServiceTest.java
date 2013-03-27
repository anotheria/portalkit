package net.anotheria.portalkit.services.accountlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

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
	public void testGetListAlwaysEmpty() {
		try {
			AccountId accId = AccountId.generateNew();

			List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "visited");
			// System.out.println("got " + accIdList.size() + " records...");
			assertNotNull(accIdList);
			assertTrue(accIdList.isEmpty());
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	private void checkCreationTimeStamp(List<AccountIdAdditionalInfo> accIdList) {
		for (AccountIdAdditionalInfo info : accIdList) {
			assertTrue("creation timestamp is empty", info.getCreationTimestamp() != 0);
		}
	}

	@Test
	public void testAddTargetToList() {
		try {
			AccountId accId = AccountId.generateNew();

			boolean res = service.addToList(accId, "contacts", new AccountIdAdditionalInfo(AccountId.generateNew(), "first"),
					new AccountIdAdditionalInfo(AccountId.generateNew(), "second"), new AccountIdAdditionalInfo(AccountId.generateNew(), "third"));
			assertTrue(res);

			List<AccountIdAdditionalInfo> accIdList = service.getList(accId, "contacts");
			// System.out.println(accIdList);
			checkCreationTimeStamp(accIdList);

			assertNotNull(accIdList);
			assertEquals(3, accIdList.size());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUpdateTargetToList() {
		try {
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
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRemoveTargetFromList() {
		try {
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
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testReverseList() {
		try {
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

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
