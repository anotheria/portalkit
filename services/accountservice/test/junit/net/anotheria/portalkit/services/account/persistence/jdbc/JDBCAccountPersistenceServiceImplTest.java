package net.anotheria.portalkit.services.account.persistence.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;

/**
 * JDBCAccountPersistenceServiceImplTest.
 * 
 * @author lrosenberg
 * @since 06.01.13 01:22
 */
abstract class JDBCAccountPersistenceServiceImplTest {

	protected JDBCAccountPersistenceServiceImpl getService(String environment) throws AccountPersistenceServiceException {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
		JDBCAccountPersistenceServiceImpl service = new JDBCAccountPersistenceServiceImpl();
		try {
			service.cleanupFromUnitTests();
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
		return service;
	}

	public Account createAccount(JDBCAccountPersistenceServiceImpl service) throws AccountPersistenceServiceException {
		Account newAcc = createAccountTemplate();
		service.saveAccount(newAcc);

		newAcc = service.getAccount(newAcc.getId());
		service.saveAccount(newAcc);

		return newAcc;
	}

	public Account createAccountTemplate() {
		Account account = new Account();
		account.setName("test");
		account.setEmail("test@example.com");
		account.setType(1);
		account.setRegistrationTimestamp(System.currentTimeMillis());
		account.addStatus(1);
		account.addStatus(16);
		account.addStatus(32);
		return Account.newAccountFromPattern(account);
	}

	public void testGetAccount(JDBCAccountPersistenceServiceImpl service) throws AccountPersistenceServiceException {
		AccountId any = AccountId.generateNew();
		Account nonExisting = service.getAccount(any);
		assertNull(nonExisting);

		Account created = createAccount(service);
		Account existing = service.getAccount(created.getId());
		assertNotSame(created, existing);
		assertEquals(created, existing);
	}

	public void testDeleteAccount(JDBCAccountPersistenceServiceImpl service) throws AccountPersistenceServiceException {
		AccountId any = AccountId.generateNew();
		service.deleteAccount(any);

		// sofar so good, we can delete nonexisting accounts.

		Account created = createAccount(service);
		Account existing = service.getAccount(created.getId());
		assertEquals(created.getId(), existing.getId());

		service.deleteAccount(existing.getId());

		Account nonexisting = service.getAccount(created.getId());
		assertNull("expected null", nonexisting);
	}

	public void testEditAccount(JDBCAccountPersistenceServiceImpl service) throws AccountPersistenceServiceException {

		Account created = createAccount(service);
		Account existing = service.getAccount(created.getId());
		assertEquals(created.getId(), existing.getId());
		assertEquals(created.getType(), existing.getType());

		existing.setType(100);
		service.saveAccount(existing);
		Account reread = service.getAccount(created.getId());
		assertEquals(created.getId(), reread.getId());
		assertEquals(100, reread.getType());
	}

	public void testAccountFields(JDBCAccountPersistenceServiceImpl service) throws AccountPersistenceServiceException {
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

	public void testGetByEmailAndGetByName(JDBCAccountPersistenceServiceImpl service) throws AccountPersistenceServiceException {
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
