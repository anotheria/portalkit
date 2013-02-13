package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.accountlist.AbstractAccountListServiceTest;
import net.anotheria.portalkit.services.accountlist.AccountListService;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;

public class AccountListServiceJDBCTest extends AbstractAccountListServiceTest {

	@Before
	@After
	public void reset() {
		
//		System.out.println("**************************************************");
		
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());

		try {
			setService(MetaFactory.get(AccountListService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}

	}

//	@Test
//	public void testGetListAlwaysEmpty() throws Exception {
//
//		AccountId accId = AccountId.generateNew();
//
//		List<AccountId> accIdList = service.getList(accId, "visited");
//		assertNotNull(accIdList);
//		assertTrue(accIdList.isEmpty());
//	}
//
//	@Test
//	public void testAddTargetToList() throws Exception {
//
//		AccountId accId = AccountId.generateNew();
//
//		boolean res = service.addToList(accId, "contacts", AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
//		assertTrue(res);
//
//		List<AccountId> accIdList = service.getList(accId, "contacts");
//		assertNotNull(accIdList);
//		assertEquals(3, accIdList.size());
//
//	}
//
//	@Test
//	public void testRemoveTargetFromList() throws Exception {
//
//		AccountId accId = AccountId.generateNew();
//
//		AccountId[] accList = new AccountId[] { AccountId.generateNew(), AccountId.generateNew() };
//
//		boolean res = service.addToList(accId, "favourites", AccountId.generateNew(), accList);
//		assertTrue(res);
//
//		List<AccountId> accIdList = service.getList(accId, "favourites");
//		System.out.println("got "+accIdList.size()+" records after insert...");
//		assertNotNull(accIdList);
//		assertEquals(3, accIdList.size());
//
//		res = service.removeFromList(accId, "favourites", Arrays.asList(accList));
//		assertTrue(res);
//		
//		accIdList = service.getList(accId, "favourites");
//		assertNotNull(accIdList);
//		assertEquals(1, accIdList.size());
//
//	}
//	
//	@Test
//	public void testReverseList() throws Exception {
//
//		AccountId accId = AccountId.generateNew();
//		
//		AccountId reverseAccId = AccountId.generateNew();
//
//		boolean res = service.addToList(accId, "visits", AccountId.generateNew(), AccountId.generateNew(), reverseAccId, AccountId.generateNew());
//		assertTrue(res);
//		
//		AccountId accId2 = AccountId.generateNew();
//		res = service.addToList(accId2, "visits", AccountId.generateNew(), AccountId.generateNew(), reverseAccId, AccountId.generateNew());
//		assertTrue(res);
//
//		List<AccountId> accIdList = service.getList(accId, "visits");
//		assertNotNull(accIdList);
//		assertEquals(4, accIdList.size());
//		
//		accIdList = service.reverseLookup(reverseAccId, "visits");
//		assertNotNull(accIdList);
//		assertEquals(2, accIdList.size());
//
//	}
	
}
