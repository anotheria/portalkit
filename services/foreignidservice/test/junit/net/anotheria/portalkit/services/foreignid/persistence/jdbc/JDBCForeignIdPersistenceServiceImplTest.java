package net.anotheria.portalkit.services.foreignid.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.ForeignIdSources;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Assert;
import org.junit.Test;

public class JDBCForeignIdPersistenceServiceImplTest {

	protected JDBCForeignIdPersistenceServiceImpl getService(String environment) throws Exception {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
		JDBCForeignIdPersistenceServiceImpl service = new JDBCForeignIdPersistenceServiceImpl();
		return service;
	}

	@Test
	public void testLink() throws Exception {
		JDBCForeignIdPersistenceServiceImpl service = getService("h2");
		ForeignId fid = new ForeignId(new AccountId("accid1"), ForeignIdSources.FACEBOOK, "foreignid1");
		service.link(fid.getAccountId(), fid.getSourceId(), fid.getId());
	}

	@Test
	public void testGetAccountIdByForeignId() throws Exception {
		JDBCForeignIdPersistenceServiceImpl service = getService("h2");
		
		service.link(new AccountId("accid1"), ForeignIdSources.FACEBOOK, "foreignid2");
		
		AccountId accid = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "foreignid2");
		Assert.assertNotNull(accid);
		Assert.assertEquals("accid1", accid.getInternalId());
	}

	@Test
	public void testGetAccountIdByUnknownForeignId() throws Exception {
		JDBCForeignIdPersistenceServiceImpl service = getService("h2");
		AccountId accid = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "unknownfid");
		Assert.assertNull(accid);
	}
	
	@Test
	public void testUnlink() throws Exception {
		JDBCForeignIdPersistenceServiceImpl service = getService("h2");
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
