package net.anotheria.portalkit.services.accountlist.persistence.inmemory;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.accountlist.AbstractAccountListServiceTest;
import net.anotheria.portalkit.services.accountlist.AccountListService;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;

public class AccountListServiceInMemoryTest extends AbstractAccountListServiceTest {

	@Before
	@After
	public void reset() {

//		System.out.println("**************************************************");

		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());

		try {
			setService(MetaFactory.get(AccountListService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}

	}

}
