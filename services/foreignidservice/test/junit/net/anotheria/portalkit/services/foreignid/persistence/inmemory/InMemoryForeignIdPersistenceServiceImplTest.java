package net.anotheria.portalkit.services.foreignid.persistence.inmemory;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.ForeignIdSources;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InMemoryForeignIdPersistenceServiceImplTest {

	@Before
	public void setup() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
	}
	
	protected InMemoryForeignIdPersistenceServiceImpl getService() throws Exception {
		return new InMemoryForeignIdPersistenceServiceImpl();
	}
	
	@Test
	public void testLink() throws Exception {
		InMemoryForeignIdPersistenceServiceImpl service = getService();
		ForeignId fid = new ForeignId(new AccountId("accid1"), ForeignIdSources.FACEBOOK, "foreignid1");
		service.link(fid.getAccountId(), fid.getSourceId(), fid.getId());
	}

	@Test
	public void testGetAccountIdByForeignId() throws Exception {
		InMemoryForeignIdPersistenceServiceImpl service = getService();
		
		service.link(new AccountId("accid1"), ForeignIdSources.FACEBOOK, "foreignid2");
		
		AccountId accid = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "foreignid2");
		Assert.assertNotNull(accid);
		Assert.assertEquals("accid1", accid.getInternalId());
	}

	@Test
	public void testGetAccountIdByUnknownForeignId() throws Exception {
		InMemoryForeignIdPersistenceServiceImpl service = getService();
		AccountId accid = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "unknownfid");
		Assert.assertNull(accid);
	}
	
	@Test
	public void testUnlink() throws Exception {
		InMemoryForeignIdPersistenceServiceImpl service = getService();
		AccountId accid = AccountId.generateNew();
		service.link(accid, ForeignIdSources.FACEBOOK, "foreignid3");
		
		AccountId accid1 = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "foreignid3");
		Assert.assertNotNull(accid1);
		Assert.assertEquals(accid.getInternalId(), accid1.getInternalId());
		
		service.unlink(accid, ForeignIdSources.FACEBOOK, "foreignid3");
		
		AccountId accid2 = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "foreignid3");
		Assert.assertNull(accid2);
		
	}

}
