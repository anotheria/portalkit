package net.anotheria.portalkit.services.account.persistence.mongo;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MongoAccountPersistenceServiceImplTest.
 */
@Ignore
public class MongoAccountPersistenceServiceImplTest {


	@Test
	public void createAccount() throws AccountPersistenceServiceException {
		MongoAccountPersistenceServiceImpl service = new MongoAccountPersistenceServiceImpl();
		Account newAcc = createAccountTemplate();
		service.saveAccount(newAcc);

		newAcc = service.getAccount(newAcc.getId());
		service.saveAccount(newAcc);
	}

	public Account createAccountTemplate() {
		Account account = new Account();
		account.setId(AccountId.generateNew());
		account.setName("test");
		account.setEmail("test@example.com");
		account.setType(1);
		account.setTenant("test");
		account.setRegistrationTimestamp(System.currentTimeMillis());
		account.addStatus(1);
		account.addStatus(16);
		account.addStatus(32);
		return account;
	}

	public Account createAccountTemplateWithId(AccountId accountId) {
		Account account = new Account(accountId);
		account.setName("test");
		account.setEmail("test@example.com");
		account.setType(1);
		account.setTenant("test");
		account.setRegistrationTimestamp(System.currentTimeMillis());
		account.addStatus(1);
		account.addStatus(16);
		account.addStatus(32);
		return account;
	}

	@Test
	public void testGetAccount() throws AccountPersistenceServiceException {
		MongoAccountPersistenceServiceImpl service = new MongoAccountPersistenceServiceImpl();
		AccountId any = AccountId.generateNew();
		Account nonExisting = service.getAccount(any);
		assertNull(nonExisting);

		Account created = createAccountTemplateWithId(any);
		service.saveAccount(created);

		Account existing = service.getAccount(any);
		assertNotSame(created, existing);
		assertEquals(created, existing);
	}

	@Test
	public void testDeleteAccount() throws AccountPersistenceServiceException {
		MongoAccountPersistenceServiceImpl service = new MongoAccountPersistenceServiceImpl();
		AccountId any = AccountId.generateNew();
		service.deleteAccount(any);

		Account created = createAccountTemplateWithId(any);
		service.saveAccount(created);

		Account existing = service.getAccount(created.getId());
		assertEquals(created.getId(), existing.getId());

		service.deleteAccount(existing.getId());

		Account nonexisting = service.getAccount(created.getId());
		assertNull("expected null", nonexisting);
	}

	@Test
	public void testEditAccount() throws AccountPersistenceServiceException {
		MongoAccountPersistenceServiceImpl service = new MongoAccountPersistenceServiceImpl();
		AccountId any = AccountId.generateNew();

		Account created = createAccountTemplateWithId(any);
		service.saveAccount(created);


		Account existing = service.getAccount(created.getId());
		assertEquals(created.getId(), existing.getId());
		assertEquals(created.getType(), existing.getType());

		existing.setType(100);
		service.saveAccount(existing);
		Account reread = service.getAccount(created.getId());
		assertEquals(created.getId(), reread.getId());
		assertEquals(100, reread.getType());
	}

	@Test
	public void testAccountFields() throws AccountPersistenceServiceException {
		MongoAccountPersistenceServiceImpl service = new MongoAccountPersistenceServiceImpl();
		Account toCreate = createAccountTemplate();
		service.saveAccount(toCreate);
		Account fromService = service.getAccount(toCreate.getId());

		assertEquals(toCreate, fromService);
		assertNotSame(toCreate, fromService);

		assertEquals("id mismatch", toCreate.getId(), fromService.getId());
		assertEquals("name mismatch", toCreate.getName(), fromService.getName());
		assertEquals("type mismatch", toCreate.getType(), fromService.getType());
		assertEquals("email mismatch", toCreate.getEmail(), fromService.getEmail());
		assertEquals("status mismatch", toCreate.getStatus(), fromService.getStatus());
		assertEquals("reg timestamp mismatch", toCreate.getRegistrationTimestamp(), fromService.getRegistrationTimestamp());
	}

	@Test
	public void testGetByEmailAndGetByName() throws AccountPersistenceServiceException {
		MongoAccountPersistenceServiceImpl service = new MongoAccountPersistenceServiceImpl();
		Account toCreate1 = createAccountTemplate();
		toCreate1.setEmail("foo@example.com");
		toCreate1.setName("foo");

		Account toCreate2 = createAccountTemplate();
		toCreate2.setEmail("foo2@example.com");
		toCreate2.setName("foo2");

		service.saveAccount(toCreate1);
		service.saveAccount(toCreate2);

		assertNull(service.getIdByName("none"));
		assertNull(service.getIdByEmail("none"));

		// check first element by name
		assertEquals(toCreate1.getId(), service.getIdByName("foo"));
		assertFalse(toCreate1.getId().equals(service.getIdByName("foo2")));
		assertFalse(toCreate1.getId().equals(service.getIdByName("foo@example.com")));

		// check first element by email
		assertEquals(toCreate1.getId(), service.getIdByEmail("foo@example.com"));
		assertFalse(toCreate1.getId().equals(service.getIdByEmail("foo2")));
		assertFalse(toCreate1.getId().equals(service.getIdByEmail("foo")));
		assertFalse(toCreate1.getId().equals(service.getIdByEmail("foo2@example.com")));

		// test for empty spaces.
		Account toCreate3 = createAccountTemplate();
		toCreate3.setEmail("foo bar@example.com");
		toCreate3.setName("foo bar");
		service.saveAccount(toCreate3);
		assertEquals(toCreate3.getId(), service.getIdByName("foo bar"));
		assertEquals(toCreate3.getId(), service.getIdByEmail("foo bar@example.com"));

	}
}
