package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.accountlist.AbstractAccountListServiceTest;
import net.anotheria.portalkit.services.accountlist.AccountListService;
import net.anotheria.portalkit.services.accountlist.AccountListServiceFactory;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;

/**
 * 
 * @author dagafonov
 *
 */
public class AccountListServiceJDBCTest extends AbstractAccountListServiceTest {

	@Before
	@After
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		MetaFactory.reset();
		
		MetaFactory.addFactoryClass(AccountListService.class, Extension.LOCAL, AccountListServiceFactory.class);
		MetaFactory.addAlias(AccountListService.class, Extension.LOCAL);
		
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());
		try {
			setService(MetaFactory.get(AccountListService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
	}

}
