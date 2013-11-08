package net.anotheria.portalkit.services.account;

import junit.framework.Assert;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * AccountServiceTest.
 *
 * @author lrosenberg
 * @since 12.12.12 22:36
 */
public class AccountServiceTest {

	@After
	@Before
	public void setup() {
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

		MetaFactory.addFactoryClass(AccountService.class, Extension.LOCAL, AccountServiceFactory.class);
		MetaFactory.addAlias(AccountService.class, Extension.LOCAL);
	}

	// this test will be removed later, for now it 'tests' the new metafactory
	// functionality, instantiation of service
	// without a factory
	@Test
	public void createAccountService() throws MetaFactoryException {
		AccountService service = MetaFactory.get(AccountService.class);
		assertNotNull(service);
	}

	@Test
	public void testNotExistingAccount() throws MetaFactoryException, AccountServiceException {
		AccountId newAccountId = AccountId.generateNew();
		AccountService service = MetaFactory.get(AccountService.class);
		try {
			Account existing = service.getAccount(newAccountId);
			fail("Exception expected");
		} catch (AccountNotFoundException e) {
			// this exception is expected
		}
		try {
			service.deleteAccount(newAccountId);
			fail("Exception expected");
		} catch (AccountNotFoundException e) {
			// this exception is expected
		}
	}

	@Test
	public void testGetAccounts() throws MetaFactoryException, AccountServiceException {
		AccountId first, second, third;
		first = AccountId.generateNew();
		second = AccountId.generateNew();
		third = AccountId.generateNew();

		ArrayList<AccountId> accountIds = new ArrayList<AccountId>();
		accountIds.add(first);
		accountIds.add(second);
		accountIds.add(third);

		AccountService service = MetaFactory.get(AccountService.class);
		List<Account> accounts1 = service.getAccounts(accountIds);
		assertNotNull(accounts1);
		for (Account acc : accounts1) {
			assertSame(NullAccount.INSTANCE, acc);
		}

		Account newAccount = service.createAccount(new Account());
		accountIds.add(newAccount.getId());

		List<Account> accounts2 = service.getAccounts(accountIds);
		assertNotNull(accounts2);
		// counting the accounts in the list, first 3 are null accounts, last
		// isnt.
		int counter = 0;
		for (Account acc : accounts2) {
			if (counter++ == 3) {
				checkPropertiesEquals(newAccount, acc);
			} else {
				assertSame(NullAccount.INSTANCE, acc);
			}
		}
	}

	/**
	 * Check that all properties of two incoming {@link Account} instance are equals.
	 *
	 * @param account
	 * 		{@link Account}
	 * @param secondAccount
	 * 		{@link Account}
	 */
	private void checkPropertiesEquals(final Account account, final Account secondAccount) {
		Assert.assertNotNull("account is null", account);
		Assert.assertNotNull("secondAccount is null", secondAccount);

		Assert.assertEquals("Ids are not equals", account.getId(), secondAccount.getId());
		Assert.assertEquals("Emails are not equals", account.getEmail(), secondAccount.getEmail());
		Assert.assertEquals("Names are not equals", account.getName(), secondAccount.getName());
		Assert.assertEquals("Registration times  are not equals", account.getRegistrationTimestamp(), secondAccount.getRegistrationTimestamp());
		Assert.assertEquals("Statuses  are not equals", account.getStatus(), secondAccount.getStatus());
		Assert.assertEquals("Types  are not equals", account.getType(), secondAccount.getType());

	}

	@Test
	public void testGetByName() throws AccountServiceException, MetaFactoryException {
		AccountService service = MetaFactory.get(AccountService.class);
		Account toCreate = new Account();
		toCreate.setName("petrov");
		service.createAccount(toCreate);

		AccountId pId = service.getAccountIdByName("petrov");
		assertNotNull(pId);

		try {
			service.getAccountIdByName("sidorov");
			fail("Exception expected");
		} catch (AccountNotFoundException e) {

		}
	}
}
