package net.anotheria.portalkit.apis.online;

import junit.framework.Assert;
import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoplass.api.APIException;
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
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.OnlineService;
import net.anotheria.portalkit.services.online.OnlineServiceException;
import net.anotheria.portalkit.services.online.OnlineServiceFactory;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.inmemory.InMemoryActivityPersistenceServiceFactory;
import net.anotheria.util.IdCodeGenerator;
import net.anotheria.util.TimeUnit;
import org.junit.Test;

/**
 * Junit which only test communications with the OnlineService and integration with other API's....
 *
 * @author h3llka
 */
public class OnlineAPIImplIntegrationTest {


    @Test
    public void testSyncLoginNotifications() {
        OnlineAPIConfiguration config = OnlineAPIConfiguration.getInstance();
        //for Async stuff disabled!!!
        config.setPerformLoginNotificationAsync(false);
        config.setPerformLogoutNotificationAsync(false);        
        config.setPerformActivityUpdateNotificationAsync(false);
        // changing update interval to 1ms
        config.setActivityUpdateInterval(1L);

        AccountId account = AccountId.generateNew();
        OnlineAPI testAPI = getAPI();
        try {
            Assert.assertFalse("Should be offline!", testAPI.isOnline(account));

            // logging IN!
            LoginAPI loginAPI = APIFinder.findAPI(LoginAPI.class);
            loginAPI.logInUser(account.getInternalId());

            Assert.assertTrue("Should be online now!!!", testAPI.isOnline(account));

            long loginTime = testAPI.readLastLoginTime(account);
            long activityTime = testAPI.readLastActivityTime(account);

            //should wait a while --- cause activity time  will be almost same!
            Thread.sleep(150);
            //call login once more! To check if updateActivity on logged in user will work
            loginAPI.logInUser(account.getInternalId());

            long loginTime2 = testAPI.readLastLoginTime(account);
            long activityTime2 = testAPI.readLastActivityTime(account);

            Assert.assertEquals("Login time differs! Error", loginTime, loginTime2);
            Assert.assertTrue("Error! activity time was not increased", activityTime2 > activityTime);


        } catch (OnlineAPIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (APIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (InterruptedException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        }
    }

    @Test
    public void testAsyncLoginNotifications() {
        OnlineAPIConfiguration config = OnlineAPIConfiguration.getInstance();
        //for Async stuff enabled!!!
        config.setPerformLoginNotificationAsync(true);
        config.setPerformLogoutNotificationAsync(true);        
        config.setPerformActivityUpdateNotificationAsync(true);
        // changing update interval to 1ms
        config.setActivityUpdateInterval(1L);

        AccountId account = AccountId.generateNew();
        OnlineAPI testAPI = getAPI();
        try {
            Assert.assertFalse("Should be offline!", testAPI.isOnline(account));

            // logging IN!
            LoginAPI loginAPI = APIFinder.findAPI(LoginAPI.class);
            loginAPI.logInUser(account.getInternalId());

            //we should wait a bit! cause  in  async mode it's not so fast ))
            Thread.sleep(400L);
            Assert.assertTrue("Should be online now!!!", testAPI.isOnline(account));

            long loginTime = testAPI.readLastLoginTime(account);
            long activityTime = testAPI.readLastActivityTime(account);

            //should wait a while --- cause activity time  will be almost same!
            Thread.sleep(150L);
            //call login once more! To check if updateActivity on logged in user will work
            loginAPI.logInUser(account.getInternalId());

            Thread.sleep(400L); // waiting will call to the Online service will be performed in async!
            long loginTime2 = testAPI.readLastLoginTime(account);
            long activityTime2 = testAPI.readLastActivityTime(account);


            Assert.assertEquals("Login time differs! Error", loginTime, loginTime2);
            Assert.assertTrue("Error! activity time was not increased", activityTime2 > activityTime);


        } catch (OnlineAPIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (APIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (InterruptedException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        }
    }


    @Test
    public void testSyncActivityNotifications() {
        OnlineAPIConfiguration config = OnlineAPIConfiguration.getInstance();
        //for Async stuff disabled!!!
        config.setPerformLoginNotificationAsync(false);
        config.setPerformLogoutNotificationAsync(false);        
        config.setPerformActivityUpdateNotificationAsync(false);
        // changing update interval to 1ms
        config.setActivityUpdateInterval(1L);

        AccountId account = AccountId.generateNew();
        OnlineAPI testAPI = getAPI();
        try {
            Assert.assertFalse("Should be offline!", testAPI.isOnline(account));

            // sending activity notification
            // logging IN!
            LoginAPI loginAPI = APIFinder.findAPI(LoginAPI.class);
            loginAPI.logInUser(account.getInternalId());

            Assert.assertTrue("Should be online now!!!", testAPI.isOnline(account));

            long loginTime = testAPI.readLastLoginTime(account);
            long activityTime = testAPI.readLastActivityTime(account);

            Thread.sleep(150);
            ActivityAPI activityAPI = APIFinder.findAPI(ActivityAPI.class);
            activityAPI.notifyMyActivity("new h3ll's URL!");

            long loginTime2 = testAPI.readLastLoginTime(account);
            long activityTime2 = testAPI.readLastActivityTime(account);

            Assert.assertEquals("Login time differs! Error", loginTime, loginTime2);
            Assert.assertTrue("Error! activity time was not increased", activityTime2 > activityTime);


            //now  -   let's imagine that user was cleaned up from the Online storage
            OnlineService onlineService = MetaFactory.get(OnlineService.class);
            onlineService.notifyLoggedOut(account);

            Assert.assertFalse("Should be offline", testAPI.isOnline(account));

            ///
            Thread.sleep(150L);
            //But user is logged IN! so next call  should login us again!
            activityAPI.notifyMyActivity("new h3ll's URL 2!");
            long loginTime3 = testAPI.readLastLoginTime(account);
            long activityTime3 = testAPI.readLastActivityTime(account);

            Assert.assertTrue("Error! activity time was not increased", activityTime3 > activityTime2);
            Assert.assertTrue("Error! login time was not increased", loginTime3 > loginTime2);


            // change activity update time! and  let's try o update once again!
            config.setActivityUpdateInterval(TimeUnit.MINUTE.getMillis());

            activityAPI.notifyMyActivity("new h3ll's URL 3!");
            long loginTime4 = testAPI.readLastLoginTime(account);
            long activityTime4 = testAPI.readLastActivityTime(account);

            Assert.assertEquals("Ups! ----  update should not be performed", loginTime4, loginTime3);
            Assert.assertEquals("Ups! ----  update should not be performed", activityTime4, activityTime3);


        } catch (MetaFactoryException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (OnlineServiceException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (OnlineAPIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (APIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (InterruptedException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        }
    }

    @Test
    public void testAsyncActivityNotifications() {
        OnlineAPIConfiguration config = OnlineAPIConfiguration.getInstance();
        //for Async stuff disabled!!!
        config.setPerformLoginNotificationAsync(false);
        config.setPerformLogoutNotificationAsync(false);        
        config.setPerformActivityUpdateNotificationAsync(false);
        // changing update interval to 1ms
        config.setActivityUpdateInterval(1L);

        AccountId account = AccountId.generateNew();
        OnlineAPI testAPI = getAPI();
        try {
            Assert.assertFalse("Should be offline!", testAPI.isOnline(account));

            // sending activity notification
            // logging IN!
            LoginAPI loginAPI = APIFinder.findAPI(LoginAPI.class);
            loginAPI.logInUser(account.getInternalId());

            Thread.sleep(400L); //waiting  for  Async call
            Assert.assertTrue("Should be online now!!!", testAPI.isOnline(account));

            long loginTime = testAPI.readLastLoginTime(account);
            long activityTime = testAPI.readLastActivityTime(account);

            Thread.sleep(150);
            ActivityAPI activityAPI = APIFinder.findAPI(ActivityAPI.class);
            activityAPI.notifyMyActivity("new h3ll's URL!");

            Thread.sleep(400L); //waiting  for  Async call
            long loginTime2 = testAPI.readLastLoginTime(account);
            long activityTime2 = testAPI.readLastActivityTime(account);

            Assert.assertEquals("Login time differs! Error", loginTime, loginTime2);
            Assert.assertTrue("Error! activity time was not increased", activityTime2 > activityTime);


            //now  -   let's imagine that user was cleaned up from the Online storage
            OnlineService onlineService = MetaFactory.get(OnlineService.class);
            onlineService.notifyLoggedOut(account);

            Assert.assertFalse("Should be offline", testAPI.isOnline(account));

            ///
            Thread.sleep(150L);
            //But user is logged IN! so next call  should login us again!
            activityAPI.notifyMyActivity("new h3ll's URL 2!");

            Thread.sleep(400L); //waiting  for  Async call
            long loginTime3 = testAPI.readLastLoginTime(account);
            long activityTime3 = testAPI.readLastActivityTime(account);

            Assert.assertTrue("Error! activity time was not increased", activityTime3 > activityTime2);
            Assert.assertTrue("Error! login time was not increased", loginTime3 > loginTime2);


            // change activity update time! and  let's try o update once again!
            config.setActivityUpdateInterval(TimeUnit.MINUTE.getMillis());

            activityAPI.notifyMyActivity("new h3ll's URL 3!");

            Thread.sleep(400L); //waiting  for  Async call
            long loginTime4 = testAPI.readLastLoginTime(account);
            long activityTime4 = testAPI.readLastActivityTime(account);

            Assert.assertEquals("Ups! ----  update should not be performed", loginTime4, loginTime3);
            Assert.assertEquals("Ups! ----  update should not be performed", activityTime4, activityTime3);


        } catch (MetaFactoryException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (OnlineServiceException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (OnlineAPIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (APIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (InterruptedException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        }
    }


    @Test
    public void testSyncLOGOUTNotifications() {
        OnlineAPIConfiguration config = OnlineAPIConfiguration.getInstance();
        //for Async stuff disabled!!!
        config.setPerformLoginNotificationAsync(false);
        config.setPerformLogoutNotificationAsync(false);        
        config.setPerformActivityUpdateNotificationAsync(false);
        // changing update interval to 1ms
        config.setActivityUpdateInterval(1L);

        AccountId account = AccountId.generateNew();
        OnlineAPI testAPI = getAPI();

        try {
            Assert.assertFalse("Should be offline!", testAPI.isOnline(account));

            // sending activity notification
            // logging IN!
            LoginAPI loginAPI = APIFinder.findAPI(LoginAPI.class);
            loginAPI.logInUser(account.getInternalId());

            Assert.assertTrue("Should be online now!!!", testAPI.isOnline(account));

            long loginTime = testAPI.readLastLoginTime(account);
            long activityTime = testAPI.readLastActivityTime(account);

            Assert.assertTrue(loginTime > 0);
            Assert.assertTrue(activityTime > 0);


            loginAPI.logoutMe();

            Assert.assertFalse("Should be offline! now!!!", testAPI.isOnline(account));


        } catch (OnlineAPIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (APIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        }

    }

    @Test
    public void testASyncLOGOUTNotifications() {
        OnlineAPIConfiguration config = OnlineAPIConfiguration.getInstance();
        //for Async stuff disabled!!!
        config.setPerformLoginNotificationAsync(true);
        config.setPerformLogoutNotificationAsync(true);        
        config.setPerformActivityUpdateNotificationAsync(true);
        // changing update interval to 1ms
        config.setActivityUpdateInterval(1L);

        AccountId account = AccountId.generateNew();
        OnlineAPI testAPI = getAPI();

        try {
            Assert.assertFalse("Should be offline!", testAPI.isOnline(account));

            // sending activity notification
            // logging IN!
            LoginAPI loginAPI = APIFinder.findAPI(LoginAPI.class);
            loginAPI.logInUser(account.getInternalId());

            Thread.sleep(400L); //waiting for Async call!
            Assert.assertTrue("Should be online now!!!", testAPI.isOnline(account));

            long loginTime = testAPI.readLastLoginTime(account);
            long activityTime = testAPI.readLastActivityTime(account);

            Assert.assertTrue(loginTime > 0);
            Assert.assertTrue(activityTime > 0);


            loginAPI.logoutMe();
            Thread.sleep(400L); //waiting for Async call!
            Assert.assertFalse("Should be offline! now!!!", testAPI.isOnline(account));


        } catch (OnlineAPIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (APIException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        } catch (InterruptedException e) {
            Assert.fail("Unexpected exception occurred " + e.getMessage());
        }

    }

    private OnlineAPI getAPI() {
        MetaFactory.reset();
        APIFinder.cleanUp();

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
            @SuppressWarnings("deprecation")
			APISession session = APISessionManager.getInstance().obtainSession(IdCodeGenerator.generateCode());
            APICallContext.getCallContext().setCurrentSession(session);
        } catch (APISessionCreationException e) {
            throw new RuntimeException("APISession initialization failed", e);
        }
    }

}
