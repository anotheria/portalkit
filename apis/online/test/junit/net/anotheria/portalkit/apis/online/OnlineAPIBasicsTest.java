package net.anotheria.portalkit.apis.online;

import junit.framework.Assert;
import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anoplass.api.activity.ActivityAPI;
import net.anotheria.anoplass.api.activity.ActivityAPIFactory;
import net.anotheria.anoplass.api.generic.login.LoginAPI;
import net.anotheria.anoplass.api.generic.login.LoginAPIFactory;
import net.anotheria.anoplass.api.generic.observation.ObservationAPI;
import net.anotheria.anoplass.api.generic.observation.ObservationAPIFactory;
import net.anotheria.anoplass.api.session.APISession;
import net.anotheria.anoplass.api.session.APISessionCreationException;
import net.anotheria.anoplass.api.session.APISessionManager;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.apis.online.OnlineAccountReadCriteriaAO.Builder;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.OnlineService;
import net.anotheria.portalkit.services.online.OnlineServiceException;
import net.anotheria.portalkit.services.online.OnlineServiceFactory;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.inmemory.InMemoryActivityPersistenceServiceFactory;
import net.anotheria.util.IdCodeGenerator;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Basic  interface methods test...
 *
 * @author h3llka
 */
public class OnlineAPIBasicsTest {

    @Test
    public void isOnlineTest() {
        OnlineAPI testAPI = getAPI();
        AccountId acc = AccountId.generateNew();

        try {
            Assert.assertFalse("Should be offline", testAPI.isOnline(acc));
        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }

        //login via service!
        loginViaService(acc);

        try {
            Assert.assertTrue("Should be online", testAPI.isOnline(acc));
        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }

        try {
            testAPI.isOnline(null);
            Assert.fail("Illegal argument");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }


    }


    @Test
    public void testLastLoginANDActivityTime() {
        OnlineAPI testAPI = getAPI();
        AccountId acc = AccountId.generateNew();

        try {
            Assert.assertEquals("Default 0 expected", 0, testAPI.readLastLoginTime(acc));
            Assert.assertEquals("Default 0 expected", 0, testAPI.readLastActivityTime(acc));

        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }

        //login via service!
        loginViaService(acc);


        try {
            Assert.assertTrue("Greater than 0 expected", 0 < testAPI.readLastLoginTime(acc));
            Assert.assertTrue("Greater than 0 expected", 0 < testAPI.readLastActivityTime(acc));

        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }

        try {
            Assert.assertTrue("Should be online", testAPI.isOnline(acc));
        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }

        try {
            testAPI.readLastLoginTime((AccountId) null);
            Assert.fail("Illegal argument");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            testAPI.readLastActivityTime((AccountId) null);
            Assert.fail("Illegal argument");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

    }


    @Test
    public void testMultipleRead() {
        List<AccountId> accountIdList = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        OnlineAPI api = getAPI();
        try {
            Map<AccountId, Long> lastLogin = api.readLastLoginTime(accountIdList);
            Map<AccountId, Long> lastActivity = api.readLastActivityTime(accountIdList);

            Assert.assertTrue(lastActivity.isEmpty());
            Assert.assertTrue(lastLogin.isEmpty());

        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }

        for (AccountId acc : accountIdList) {
            loginViaService(acc);
        }

        try {
            Map<AccountId, Long> lastLogin = api.readLastLoginTime(accountIdList);
            Map<AccountId, Long> lastActivity = api.readLastActivityTime(accountIdList);

            Assert.assertFalse(lastActivity.isEmpty());
            Assert.assertFalse(lastLogin.isEmpty());

            Assert.assertEquals(accountIdList.size(), lastActivity.size());
            Assert.assertEquals(accountIdList.size(), lastLogin.size());


            //empty calls
            Assert.assertTrue(api.readLastActivityTime(new ArrayList<AccountId>()).isEmpty());
            Assert.assertTrue(api.readLastLoginTime(new ArrayList<AccountId>()).isEmpty());

        } catch (OnlineAPIException e) {
            Assert.fail(e.getMessage());
        }


        try {
            api.readLastLoginTime((List<AccountId>) null);
            Assert.fail("Illegal argument");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            api.readLastActivityTime((List<AccountId>) null);
            Assert.fail("Illegal argument");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

    }

    @Test
    public void onlineAccountReadTEst() {

        OnlineAPI api = getAPI();

        OnlineAccountReadCriteriaAO.Builder builder = new Builder();
        builder.withASCSortingByLastActivityTime();

        try {
            Assert.assertTrue("No online users!", api.readOnlineAccounts(builder.buildCriteria()).isEmpty());
        } catch (OnlineAPIException e) {
            Assert.fail();
        }

        loginViaService(AccountId.generateNew());


        try {
            Assert.assertEquals("1 online user!", 1, api.readOnlineAccounts(builder.buildCriteria()).size());
        } catch (OnlineAPIException e) {
            Assert.fail();
        }

        try {
            api.readOnlineAccounts(null);
            Assert.fail("IllegalArgumentException argument!");
        } catch (Exception e) {

        }

    }


    /**
     * Login via service.
     *
     * @param acc
     */
    private void loginViaService(AccountId acc) {
        try {
            OnlineService online = MetaFactory.get(OnlineService.class);
            online.notifyLoggedIn(acc);
        } catch (MetaFactoryException e) {
            Assert.fail(e.getMessage());
        } catch (OnlineServiceException e) {
            Assert.fail(e.getMessage());
        }

    }


    private OnlineAPI getAPI() {
        MetaFactory.reset();
        APIFinder.cleanUp();
        BasicConfigurator.configure();

        MetaFactory.addAlias(ActivityPersistenceService.class, Extension.LOCAL);
        MetaFactory.addFactoryClass(ActivityPersistenceService.class, Extension.LOCAL, InMemoryActivityPersistenceServiceFactory.class);
        MetaFactory.addAlias(OnlineService.class, Extension.LOCAL);
        MetaFactory.addFactoryClass(OnlineService.class, Extension.LOCAL, OnlineServiceFactory.class);

        configureTestSession();

        APIFinder.addAPIFactory(ObservationAPI.class, new ObservationAPIFactory());
        APIFinder.addAPIFactory(LoginAPI.class, new LoginAPIFactory());
        APIFinder.addAPIFactory(ActivityAPI.class, new ActivityAPIFactory());
        APIFinder.addAPIFactory(OnlineAPI.class, new OnlineAPIFactory());


        return APIFinder.findAPI(OnlineAPI.class);
    }

    /**
     * Create Test - Context and Session  for  testing.
     */
    private void configureTestSession() {
        // Other Configuration For Testing
        try {
            APISession session = APISessionManager.getInstance().obtainSession(IdCodeGenerator.generateCode());
            APICallContext.getCallContext().setCurrentSession(session);
        } catch (APISessionCreationException e) {
            throw new RuntimeException("APISession initialization failed", e);
        }
    }
}
