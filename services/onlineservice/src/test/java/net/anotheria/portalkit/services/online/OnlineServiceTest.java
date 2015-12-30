package net.anotheria.portalkit.services.online;

import junit.framework.Assert;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.Builder;
import net.anotheria.portalkit.services.online.persistence.ActivityNotFoundInPersistenceServiceException;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceServiceException;
import net.anotheria.portalkit.services.online.persistence.inmemory.InMemoryActivityPersistenceServiceFactory;
import net.anotheria.portalkit.services.online.persistence.jdbc.JDBCActivityPersistenceServiceFactory;
import net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceConstants;
import net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceServiceFactory;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.StorageServiceFactory;
import net.anotheria.portalkit.services.storage.inmemory.GenericInMemoryServiceFactory;
import net.anotheria.portalkit.services.storage.type.StorageType;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Junit for Basic {@link OnlineService} functionality check.
 * <p/>
 * Test will use different persistence's  during run! (inmem + jdbc - storage based!). All persistances @ the lowest lvl  uses inmemory storage!
 *
 * @author h3llka
 */
public class OnlineServiceTest {

    @BeforeClass
    public static void before() {
        ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
        System.setProperty("JUNITTEST", "true");
    }

    /**
     * Perform login test.
     */
    @Test
    public void loggedInTest() {
        for (PersistenceServiceType p : PersistenceServiceType.values())
            loginCase(p);

    }

    @Test
    public void notifyActivityChangeTest() {
        for (PersistenceServiceType p : PersistenceServiceType.values())
            activityChangeCase(p);
    }

    @Test
    public void logOutTest() {

        for (PersistenceServiceType p : PersistenceServiceType.values())
            logOutCase(p);
    }

    @Test
    public void removeLoggedInUserData() {
        for (PersistenceServiceType p : PersistenceServiceType.values())
            removeLoggedInUserDataCase(p);
    }


    @Test
    public void readUserDataTest() {
        for (PersistenceServiceType p : PersistenceServiceType.values())
            readUserData(p);
    }


    @Test
    public void illegalArgumentsTest() {

        OnlineService service = getOnlineServiceInstance(PersistenceServiceType.INMEMORY);


        try {
            service.notifyLoggedIn(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            service.notifyLoggedOut(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            service.notifyUserActivity(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            service.readOnlineUsers(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            service.isOnline(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            service.readLastActivityTime(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            service.readLastLoginTime(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            service.readLastLogin(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            service.readLastActivity(null);
            Assert.fail("Illegal argument! passed");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }


        try {
            Assert.assertTrue("not empty ??", service.readLastLoginTime(Collections.<AccountId>emptyList()).isEmpty());
            Assert.assertTrue("not empty ??", service.readLastActivityTime(Collections.<AccountId>emptyList()).isEmpty());

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }


    }

    /**
     * Executes different read calls.
     *
     * @param p env param
     */
    private void readUserData(PersistenceServiceType p) {
        List<AccountId> users = Arrays.asList(AccountId.generateNew(), AccountId.generateNew());
        OnlineService service = getOnlineServiceInstance(p);
        //logging in!
        for (AccountId account : users)
            notifyUserComesOnline(service, account);

        // execute read..
        Builder criteriaBuilder = new Builder();
        criteriaBuilder.withASCSortingByLastLoginTime();
        try {
            List<AccountId> onlineUsersLastLoginASC = service.readOnlineUsers(criteriaBuilder.build());
            criteriaBuilder.withDESCSortingByLastActivityTime();
            List<AccountId> onlineUsersLastActivityASC = service.readOnlineUsers(criteriaBuilder.build());

            Assert.assertNotNull("NULL ? can't be ", onlineUsersLastActivityASC);
            Assert.assertNotNull("NULL ? can't be ", onlineUsersLastLoginASC);

            Assert.assertEquals("List should not differs", onlineUsersLastActivityASC, onlineUsersLastActivityASC);

            for (AccountId account : users) {
                Assert.assertTrue("Such data should present", onlineUsersLastActivityASC.contains(account));
                Assert.assertTrue("Such data should present", onlineUsersLastLoginASC.contains(account));
            }
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }

        //Creating 1 used directly via persistence.. this will allow us  to query offline users...
        AccountId persistedOfflineAccount = AccountId.generateNew();
        try {
            ActivityPersistenceService persistence = MetaFactory.get(ActivityPersistenceService.class);
            try {
                persistence.saveLastLogin(persistedOfflineAccount, System.currentTimeMillis());
            } catch (ActivityPersistenceServiceException e) {
                Assert.fail(p + " " + e.getMessage());
            }


            List<AccountId> list = new ArrayList<AccountId>(users);
            list.add(persistedOfflineAccount);
            //reading last login
            for (AccountId acc : list)
                try {
                    Assert.assertNotNull(service.readLastLogin(acc));
                } catch (OnlineServiceException e) {
                    Assert.fail(p + " " + e.getMessage());
                }

            //reading last activity
            for (AccountId acc : list)
                try {
                    Assert.assertNotNull(service.readLastActivity(acc));
                } catch (OnlineServiceException e) {
                    Assert.fail(p + " " + e.getMessage());
                }

            try {
                Map<AccountId, Long> lastLogin = service.readLastLoginTime(list);
                Map<AccountId, Long> lastActivity = service.readLastActivityTime(list);
                Assert.assertNotNull("Last activity map can't be null", lastActivity);
                Assert.assertNotNull("Last login map can't be null", lastLogin);
                Assert.assertEquals("la - Size is not 3 elements??", list.size(), lastLogin.size());
                Assert.assertEquals("ll - Size is not 3 elements??", list.size(), lastActivity.size());
            } catch (OnlineServiceException e) {
                Assert.fail(p + " " + e.getMessage());
            }

        } catch (MetaFactoryException e) {
            Assert.fail(p + " " + e.getMessage());
        }


    }

    /**
     * Removing all info about user which is online!
     *
     * @param p environment
     */
    private void removeLoggedInUserDataCase(PersistenceServiceType p) {
        final AccountId accountId = AccountId.generateNew();
        OnlineService service = getOnlineServiceInstance(p);
        //check that user is offline

        try {
            Assert.assertFalse("is online ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }

        //logging IN
        notifyUserComesOnline(service, accountId);

        //remove!
        try {
            service.removeActivityData(accountId);
            //check that Offline!
            Assert.assertFalse("Should be offline NOW", service.isOnline(accountId));
            try {
                ActivityPersistenceService persistence = MetaFactory.get(ActivityPersistenceService.class);
                try {
                    persistence.readLastActivity(accountId);
                    Assert.fail(p + " Should be error! cause  user data was removed!");
                } catch (ActivityPersistenceServiceException e) {
                    Assert.assertTrue(e instanceof ActivityNotFoundInPersistenceServiceException);
                }
                try {
                    persistence.readLastLogin(accountId);
                    Assert.fail(p + "Should be error! cause  user data was removed!");
                } catch (ActivityPersistenceServiceException e) {
                    Assert.assertTrue(e instanceof ActivityNotFoundInPersistenceServiceException);
                }

            } catch (MetaFactoryException e) {
                throw new RuntimeException(e);
            }


        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }
    }


    /**
     * Check logOutCase functionality...
     *
     * @param p env param
     */
    private void logOutCase(PersistenceServiceType p) {
        final AccountId accountId = AccountId.generateNew();
        OnlineService service = getOnlineServiceInstance(p);
        //check that user is offline

        try {
            Assert.assertFalse("is online ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }

        //  try to update activity time
        try {
            service.notifyUserActivity(accountId);
            Assert.fail(p + " Account is not online! Failure expected!");
        } catch (OnlineServiceException e) {
            Assert.assertTrue("erm ? " + e.getMessage(), e instanceof AccountIsOfflineException);
        }


        notifyUserComesOnline(service, accountId);

        //now he is online!
        try {
            Assert.assertTrue("is offline ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }

        //LOG out!
        try {
            service.notifyLoggedOut(accountId);
            //second call to simulate UserIsOfflineException case!
            try {
                service.notifyLoggedOut(accountId);
                Assert.fail(p + " Already offline! Exception expected!");
            } catch (AccountIsOfflineException e) {
            }
            Assert.assertFalse("is online ???? Logout does not works ??", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }
    }


    /**
     * Activity change test.
     *
     * @param p {@link PersistenceServiceType}
     */
    private void activityChangeCase(PersistenceServiceType p) {
        final AccountId accountId = AccountId.generateNew();
        OnlineService service = getOnlineServiceInstance(p);

        //check that user is offline
        try {
            Assert.assertFalse("is online ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }

        //  try to update activity time
        try {
            service.notifyUserActivity(accountId);
            Assert.fail(p + " Account is not online! Failure expected!");
        } catch (OnlineServiceException e) {
            Assert.assertTrue("erm ? " + e.getMessage(), e instanceof AccountIsOfflineException);
        }


        notifyUserComesOnline(service, accountId);
        //now he is online!
        try {
            Assert.assertTrue("is offline ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }
        try {
            Thread.sleep(200l);
        } catch (InterruptedException e) {
            Assert.fail(p + " " + e.getMessage());
        }
        // change  time check now.
        notifyUserActivityChange(service, accountId);
    }


    /**
     * Login functionality check.
     *
     * @param p env param
     */
    private void loginCase(PersistenceServiceType p) {
        final AccountId accountId = AccountId.generateNew();
        OnlineService service = getOnlineServiceInstance(p);

        //check that user is offline
        try {
            Assert.assertFalse("is online ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }


        notifyUserComesOnline(service, accountId);
        //now he is online!
        try {
            Assert.assertTrue("is offline ????", service.isOnline(accountId));
        } catch (OnlineServiceException e) {
            Assert.fail(p + " " + e.getMessage());
        }
    }

    /**
     * Perform  activity time update.
     *
     * @param service   {@link OnlineService}
     * @param accountId {@link AccountId}
     */
    private void notifyUserActivityChange(OnlineService service, AccountId accountId) {
        try {
            ActivityPersistenceService persistence = MetaFactory.get(ActivityPersistenceService.class);
            Long previousActivity = null;
            Long prevLastLogin = null;
            try {
                previousActivity = persistence.readLastActivity(accountId);
                prevLastLogin = persistence.readLastLogin(accountId);
            } catch (ActivityPersistenceServiceException e) {
                Assert.fail("Error! " + e.getMessage());
            }

            try {
                service.notifyUserActivity(accountId);
                long loginTime = service.readLastLogin(accountId);
                long activity = service.readLastActivity(accountId);

                Assert.assertEquals("Ups....", loginTime, prevLastLogin != null ? prevLastLogin : 0L);

                try {
                    Assert.assertEquals("Ups....", activity, persistence.readLastActivity(accountId));
                } catch (ActivityPersistenceServiceException e) {
                    Assert.fail(e.getMessage());
                }

                Assert.assertTrue("Activity time was not changed", activity >= previousActivity);
            } catch (OnlineServiceException e) {
                Assert.fail(e.getMessage());
            }
        } catch (MetaFactoryException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Performs log in...
     *
     * @param service   {@link OnlineService}
     * @param accountId {@link AccountId} id of user
     */
    private void notifyUserComesOnline(OnlineService service, AccountId accountId) {
        try {
            ActivityPersistenceService persistence = MetaFactory.get(ActivityPersistenceService.class);
            Long previousLoginTime = null;
            try {
                previousLoginTime = persistence.readLastLogin(accountId);
            } catch (ActivityPersistenceServiceException e) {
                //ignore!
            }
            try {
                service.notifyLoggedIn(accountId);
                long loginTime = service.readLastLogin(accountId);
                if (previousLoginTime != null)
                    Assert.assertTrue("Ups....", loginTime >= previousLoginTime);

                try {
                    Assert.assertEquals(loginTime, persistence.readLastLogin(accountId));
                } catch (ActivityPersistenceServiceException e) {
                    Assert.fail("TIme from service and  persistence differs.");
                }


                // second login! to check  login in case when user is online!
                try {
                    service.notifyLoggedIn(accountId);
                    Assert.fail("Exception expected ! cause user is online now");
                } catch (AccountIsOnlineException e) {
                }


            } catch (OnlineServiceException e) {
                Assert.fail(e.getMessage());
            }

        } catch (MetaFactoryException e) {
            Assert.fail(e.getMessage());
        }

        //To change body of created methods use File | Settings | File Templates.
    }


    /**
     * Init service for required {@link PersistenceServiceType}.
     *
     * @param env {@link PersistenceServiceType}
     * @return {@link OnlineService} instance
     */
    private OnlineService getOnlineServiceInstance(final PersistenceServiceType env) {
        MetaFactory.reset();
        MetaFactory.addAlias(OnlineService.class, Extension.LOCAL);
        MetaFactory.addFactoryClass(OnlineService.class, Extension.LOCAL, OnlineServiceFactory.class);
        MetaFactory.addAlias(ActivityPersistenceService.class, Extension.LOCAL);
        switch (env) {
            case INMEMORY:
                MetaFactory.addFactoryClass(ActivityPersistenceService.class, Extension.LOCAL, InMemoryActivityPersistenceServiceFactory.class);
                break;
            case JDBС:
                MetaFactory.addFactoryClass(ActivityPersistenceService.class, Extension.LOCAL, JDBCActivityPersistenceServiceFactory.class);
                break;
            case STORAGE_BASED:
                // Storage services INitialization!!!
                Map<String, Serializable> factoryParameters = new HashMap<String, Serializable>();
                factoryParameters.put(GenericInMemoryServiceFactory.PARAMETER_ENTITY_KEY_FIELD_NAME, SBActivityPersistenceConstants.ACTIVITY_PERSISTENCE_GENERIC_STORAGE_KEY_FIELD_NAME);
                factoryParameters.put(StorageServiceFactory.PARAMETER_STORAGE_TYPE, StorageType.IN_MEMORY_GENERIC);
                MetaFactory.addParameterizedFactoryClass(StorageService.class, SBActivityPersistenceConstants.ACTIVITY_PERSISTENCE_GENERIC_STORAGE_NAME, StorageServiceFactory.class, factoryParameters);
                //initing factory
                MetaFactory.addFactoryClass(ActivityPersistenceService.class, Extension.LOCAL, SBActivityPersistenceServiceFactory.class);
                break;
        }
        try {
            return MetaFactory.get(OnlineService.class);
        } catch (MetaFactoryException e) {
            throw new RuntimeException(env + " " + e.getMessage(), e);
        }
    }

    /**
     * Env for testing.
     */
    private enum PersistenceServiceType {
        INMEMORY,
        JDBС,
        STORAGE_BASED
    }

}
