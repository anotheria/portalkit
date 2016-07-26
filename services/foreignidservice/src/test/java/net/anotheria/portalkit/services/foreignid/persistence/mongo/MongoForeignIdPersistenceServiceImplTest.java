package net.anotheria.portalkit.services.foreignid.persistence.mongo;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.ForeignIdSources;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Roman Stetsiuk on 7/25/16.
 */
public class MongoForeignIdPersistenceServiceImplTest {
    protected ForeignIdPersistenceService getService(String environment) throws ForeignIdPersistenceServiceException {
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", environment));
        ForeignIdPersistenceService service = new MongoForeignIdPersistenceServiceImpl();
        return service;
    }

    @Test
    public void testLink() throws ForeignIdPersistenceServiceException {
        ForeignIdPersistenceService service = getService("mongo");
        ForeignId fid = new ForeignId(new AccountId("accid1"), ForeignIdSources.FACEBOOK, "foreignid1");
        service.link(fid.getAccountId(), fid.getSourceId(), fid.getId());
    }

    @Test
    public void testGetAccountIdByForeignId() throws ForeignIdPersistenceServiceException {
        ForeignIdPersistenceService service = getService("mongo");

        service.link(new AccountId("accid1"), ForeignIdSources.FACEBOOK, "foreignid2");

        AccountId accid = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "foreignid2");
        Assert.assertNotNull(accid);
        Assert.assertEquals("accid1", accid.getInternalId());
    }

    @Test
    public void testGetAccountIdByUnknownForeignId() throws ForeignIdPersistenceServiceException {
        ForeignIdPersistenceService service = getService("mongo");
        AccountId accid = service.getAccountIdByForeignId(ForeignIdSources.FACEBOOK, "unknownfid");
        Assert.assertNull(accid);
    }

    @Test
    public void testUnlink() throws ForeignIdPersistenceServiceException {
        ForeignIdPersistenceService service = getService("mongo");
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