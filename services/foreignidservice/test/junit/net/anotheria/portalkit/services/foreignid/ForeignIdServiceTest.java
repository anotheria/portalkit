package net.anotheria.portalkit.services.foreignid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;
import net.anotheria.util.IdCodeGenerator;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO comment this class
 * 
 * @author lrosenberg
 * @since 21.01.13 15:55
 */
// @Ignore
public class ForeignIdServiceTest {

	@Before
	@After
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		MetaFactory.reset();
		// use jdbc for persistence
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());
	}

	@Test
	public void testLinkUnlink() throws Exception {
		ForeignIdService service = MetaFactory.get(ForeignIdService.class);

		AccountId acc = AccountId.generateNew();

		List<ForeignId> ids = service.getForeignIds(acc);
		assertNotNull(ids);
		assertEquals(ids.size(), 0);

		int source = 1;
		String sourceId = IdCodeGenerator.generateCode(10);
		service.addForeignId(acc, sourceId, source);

		AccountId acc2 = service.getAccountIdByForeignId(sourceId, source);
		assertNotNull(acc2);
		assertEquals(acc, acc2);

		ids = service.getForeignIds(acc);
		assertNotNull(ids);
		assertEquals(ids.size(), 1);
		assertEquals(acc, ids.get(0).getAccountId());

		// unlink
		service.removeForeignId(acc, sourceId, source);

		assertNull(service.getAccountIdByForeignId(sourceId, source));

	}

	@Test
	public void testForeignIdUniqueness() throws Exception {
		ForeignIdService service = MetaFactory.get(ForeignIdService.class);
		AccountId acc1 = AccountId.generateNew();
		AccountId acc2 = AccountId.generateNew();

		int source = 1;
		String sourceId = IdCodeGenerator.generateCode(10);
		service.addForeignId(acc1, sourceId, source);

		try {
			service.addForeignId(acc2, sourceId, source);
			fail("Can't import the same foreign id twice");
		} catch (Exception e) {

		}
	}

}
