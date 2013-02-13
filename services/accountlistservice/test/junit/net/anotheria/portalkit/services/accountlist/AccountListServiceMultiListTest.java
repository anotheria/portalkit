package net.anotheria.portalkit.services.accountlist;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.02.13 11:24
 */
public class AccountListServiceMultiListTest {

	@Before
	@After
	public void reset() {

		System.out.println("**************************************************");

		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());

	}

	@Test public void test2lists() throws Exception{
		AccountListService service = MetaFactory.get(AccountListService.class);
		AccountId owner = AccountId.generateNew();
		AccountId friend = AccountId.generateNew();
		AccountId foe = AccountId.generateNew();
		AccountId friend2 = AccountId.generateNew();
		AccountId foe2 = AccountId.generateNew();

		assertNotNull(service.getList(owner, "friend"));
		assertNotNull(service.getList(owner, "foe"));

		service.addToList(owner, "friend", friend);
		service.addToList(owner, "foe", foe);

		assertNotNull(service.getList(owner, "friend"));
		assertNotNull(service.getList(owner, "foe"));
		assertEquals(1, service.getList(owner, "friend").size());
		assertEquals(1, service.getList(owner, "foe").size());

		service.addToList(owner, "friend", friend2);
		service.addToList(owner, "foe", foe2);

		assertNotNull(service.getList(owner, "friend"));
		assertNotNull(service.getList(owner, "foe"));
		assertEquals(2, service.getList(owner, "friend").size());
		assertEquals(2, service.getList(owner, "foe").size());


		service.addToList(owner, "friend", friend2);
		service.addToList(owner, "foe", foe2);
		assertEquals(2, service.getList(owner, "friend").size());
		assertEquals(2, service.getList(owner, "foe").size());

	}
}
