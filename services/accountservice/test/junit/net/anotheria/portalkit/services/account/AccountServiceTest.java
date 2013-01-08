package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 22:36
 */
public class AccountServiceTest {

	@After
	@Before public void setup(){
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());
	}

	//this test will be removed later, for now it 'tests' the new metafactory functionality, instantiation of service
	//without a factory
	@Test
	public void createAccountService() throws MetaFactoryException{
		AccountService service = MetaFactory.get(AccountService.class);
		assertNotNull(service);
	}

	@Test public void testNotExistingAccount() throws Exception{
		AccountId newAccountId = AccountId.generateNew();
		AccountService service = MetaFactory.get(AccountService.class);
		try{
			Account existing = service.getAccount(newAccountId);
			fail("Exception expected");
		}catch(AccountNotFoundException e){
			//this exception is expected
		}
	}

	@Test public void testGetAccounts() throws Exception{
		AccountId first, second, third;
		first = AccountId.generateNew();
		second = AccountId.generateNew();
		third = AccountId.generateNew();

		ArrayList<AccountId> accountIds = new ArrayList<AccountId>();
		accountIds.add(first);accountIds.add(second);accountIds.add(third);

		AccountService service = MetaFactory.get(AccountService.class);
		List<Account> accounts1 = service.getAccounts(accountIds);
		assertNotNull(accounts1);
		for (Account acc : accounts1){
			assertSame(NullAccount.INSTANCE, acc);
		}

		Account newAccount = service.createAccount(new Account());
		accountIds.add(newAccount.getId());

		List<Account> accounts2 = service.getAccounts(accountIds);
		assertNotNull(accounts2);
		//counting the accounts in the list, first 3 are null accounts, last isnt.
		int counter = 0;
		for (Account acc : accounts2){
			if (counter++==3){
				assertSame(newAccount, acc);
			}else{
				assertSame(NullAccount.INSTANCE, acc);
			}
		}
	}

	@Test
	public void testGetByName() throws AccountServiceException, MetaFactoryException{
		AccountService service = MetaFactory.get(AccountService.class);
		Account toCreate = new Account();
		toCreate.setName("petrov");
		service.createAccount(toCreate);

		AccountId pId = service.getAccountIdByName("petrov");
		assertNotNull(pId);

		try{
			service.getAccountIdByName("sidorov");
			fail("Exception expected");
		}catch(AccountNotFoundException e){

		}
	}

}
