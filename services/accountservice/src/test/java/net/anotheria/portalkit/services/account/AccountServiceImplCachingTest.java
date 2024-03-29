package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceService;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceServiceException;
import net.anotheria.portalkit.services.account.persistence.inmemory.InMemoryAccountPersistenceServiceImpl;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * AccountServiceImplCachingTest.
 * 
 * @author lrosenberg
 * @since 21.12.12 14:23
 */
public class AccountServiceImplCachingTest {

	/**
	 * 
	 */
	private int get, save, delete, getbyname, getbyemail;

	@Before
	@After
	public void reset() {
		get = save = delete = getbyname = getbyemail = 0;
		MetaFactory.reset();
		MetaFactory.createOnTheFlyFactory(AccountPersistenceService.class, Extension.NONE, new InMemoryAccountPersistenceServiceImpl() {

			@Override
			public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
				get++;
				return super.getAccount(id);
			}

			@Override
			public void saveAccount(Account account) throws AccountPersistenceServiceException {
				save++;
				super.saveAccount(account);
			}

			@Override
			public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
				delete++;
				super.deleteAccount(id);
			}

			@Override
			public AccountId getIdByName(String name) throws AccountPersistenceServiceException {
				getbyname++;
				return super.getIdByName(name);
			}

			@Override
			public AccountId getIdByEmail(String email) throws AccountPersistenceServiceException {
				getbyemail++;
				return super.getIdByEmail(email);
			}
		});

		MetaFactory.createOnTheFlyFactory(AccountAuditPersistenceService.class, Extension.NONE, new AccountAuditPersistenceService() {
			@Override
			public void saveAccountAudit(AccountAudit accountAudit) throws AccountAuditPersistenceServiceException {

			}

			@Override
			public List<AccountAudit> getAccountAudits(AccountId accountId) throws AccountAuditPersistenceServiceException {
				return null;
			}
		});
	}

	@Test
	public void testNotExistingCache() throws AccountServiceException {
		AccountServiceImpl service = AccountServiceImpl.INSTANCE;
		service.unitTestReset();

		AccountId id = AccountId.generateNew();
		try {
			service.getAccount(id);
			fail("exception expected");
		} catch (AccountNotFoundException e) {
			assertEquals(1, get);
		}
		try {
			service.getAccount(id);
			fail("exception expected");
		} catch (AccountNotFoundException e) {
			assertEquals("second call shouldn't produce a call to persistence", 1, get);
		}
	}

	@Test
	public void testGetByNameCache() throws AccountServiceException {
		AccountServiceImpl service = AccountServiceImpl.INSTANCE;
		service.unitTestReset();
		Account toCreate = new Account();
		toCreate.setName("petrov");
		service.createAccount(toCreate);
		assertEquals(1, getbyname);
		AccountId pId = service.getAccountIdByName("petrov");
		assertNotNull(pId);
		assertEquals(2, getbyname);
		pId = service.getAccountIdByName("petrov");
		// this should have produced no call to persistence.
		assertEquals(2, getbyname);

	}

}
