package net.anotheria.portalkit.services.account;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 22:36
 */
public class AccountServiceTest {
	//this test will be removed later, for now it 'tests' the new metafactory functionality, instantiation of service
	//without a factory
	@Test
	public void createAccountService() throws MetaFactoryException{
		AccountService service = MetaFactory.get(AccountService.class);
		assertNotNull(service);
	}
}
