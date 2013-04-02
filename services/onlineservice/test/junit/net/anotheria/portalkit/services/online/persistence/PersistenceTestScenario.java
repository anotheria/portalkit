package net.anotheria.portalkit.services.online.persistence;

import junit.framework.Assert;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Basic abstract persistence test scenario.
 *
 * @author h3llka
 */
public abstract class PersistenceTestScenario {

    @Test
    public void wrongArgumentsCheckTest() {
        try {
            ActivityPersistenceService service = getService(getEnvironments().get(0));

            try {
                service.readLastActivity((List<AccountId>) null);
                Assert.fail("Not valid argument passed");
            } catch (IllegalArgumentException e) {
            }
            try {
                service.readLastActivity((AccountId) null);
                Assert.fail("Not valid argument passed");
            } catch (IllegalArgumentException e) {
            }
            try {
                service.readLastLogin((List<AccountId>) null);
                Assert.fail("Not valid argument passed");
            } catch (IllegalArgumentException e) {
            }
            try {
                service.readLastLogin((AccountId) null);
                Assert.fail("Not valid argument passed");
            } catch (IllegalArgumentException e) {
            }

            try {
                service.saveLastActivity(null, 0);
                Assert.fail("Not valid argument passed");
            } catch (IllegalArgumentException e) {
            }
            try {
                service.saveLastLogin((AccountId) null, 0);
                Assert.fail("Not valid argument passed");
            } catch (IllegalArgumentException e) {
            }


        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void dataDeleteTest() {
        for (String env : getEnvironments())
            deleteData(env);
    }


    @Test
    public void saveLastLoginOnDifferentDbTest() {
        for (String env : getEnvironments())
            saveLastLogin(env);

    }


    @Test
    public void saveLastActivityOnDifferentDbTest() {
        for (String env : getEnvironments())
            saveLastActivity(env);
    }

    @Test
    public void readCollectionData() {
        for (String env : getEnvironments())
            testListQueryingScenario(env);
    }

    /**
     * Return environments list.
     *
     * @return
     */
    protected abstract List<String> getEnvironments();

    /**
     * Return configured service instance - for preselected environment.
     *
     * @param environment env
     * @return
     */
    protected abstract ActivityPersistenceService getService(String environment);


    /**
     * Remove data test!
     *
     * @param env environment string
     */
    private void deleteData(String env) {
        AccountId account = AccountId.generateNew();
        try {
            ActivityPersistenceService service = getService(env);
            //check that  really such data does not exists
            checkReadErrors(account, service);


            //#### Create PART
            final long time = System.nanoTime();
            createLastLoginEntry(service, account, time);
            // check  what was persisted!

            long lastLoginPersisted = service.readLastLogin(account);
            long lastActivityPersisted = service.readLastActivity(account);

            Assert.assertEquals("Should be  equals! Cause it was  Login save call ", lastLoginPersisted, lastActivityPersisted);
            Assert.assertEquals("Should be  equals! to previous time  ", lastActivityPersisted, time);
            //#### Create PART END

            //########NOW deleting!######
            service.deleteActivityEntry(account);
            //now we are sure that  data deleted!
            checkReadErrors(account, service);


        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Save last login test scenario.
     *
     * @param environment - env var
     */
    private void saveLastLogin(final String environment) {
        AccountId account = AccountId.generateNew();
        try {
            ActivityPersistenceService service = getService(environment);
            //check that  really such data does not exists
            checkReadErrors(account, service);

            final long time = System.nanoTime();
            createLastLoginEntry(service, account, time);
            //second same call required ---  simply  to check Update data possibility!!!
            createLastLoginEntry(service, account, time);
            // check  what was persisted!

            long lastLoginPersisted = service.readLastLogin(account);
            long lastActivityPersisted = service.readLastActivity(account);

            Assert.assertEquals("Should be  equals! Cause it was  Login save call ", lastLoginPersisted, lastActivityPersisted);
            Assert.assertEquals("Should be  equals! to previous time  ", lastActivityPersisted, time);


        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
        }
    }


    /**
     * Last activity update scenario!
     *
     * @param environment env
     */
    private void saveLastActivity(final String environment) {
        AccountId account = AccountId.generateNew();
        try {
            ActivityPersistenceService service = getService(environment);
            //check that  really such data does not exists
            checkReadErrors(account, service);

            long time = System.nanoTime();
            //perform last activity update!
            try {
                long entry = service.saveLastActivity(account, time);
                Assert.fail("Such account data should not exists ! ");
            } catch (ActivityPersistenceServiceException e) {
                Assert.assertTrue(e instanceof ActivityNotFoundInPersistenceServiceException);
            }

            //creating first!
            createLastLoginEntry(service, account, time);
            // check  what was persisted!
            long lastLoginPersisted = service.readLastLogin(account);
            long lastActivityPersisted = service.readLastActivity(account);
            Assert.assertEquals("Should be  equals! Cause it was  Login save call ", lastLoginPersisted, lastActivityPersisted);
            Assert.assertEquals("Should be  equals! to previous time  ", lastActivityPersisted, time);


            //updating!
            time = System.nanoTime();
            long entryTime = updateLastActivityTime(service, account, time);
            Assert.assertEquals("UPS!!!", entryTime, time);

            //comparation with Last login time!
            lastLoginPersisted = service.readLastLogin(account);
            lastActivityPersisted = service.readLastActivity(account);


            Assert.assertTrue("Activity time should be greater  that last login", lastLoginPersisted < lastActivityPersisted);
            Assert.assertEquals("Activity should be equals with the time which was passed to persist method!", lastActivityPersisted, time);


        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
        }

    }

    /**
     * List data reading scenario tests!
     *
     * @param env environment
     */
    private void testListQueryingScenario(final String env) {
        Map<AccountId, Long> lastLoginTimeMAP = new HashMap<AccountId, Long>();
        Map<AccountId, Long> lastActivityTimeMAP = new HashMap<AccountId, Long>();

        List<AccountId> accounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());

        try {
            ActivityPersistenceService service = getService(env);
            for (AccountId accountId : accounts) {
                final long loginTime = System.nanoTime() + new Random(System.currentTimeMillis()).nextLong();
                final long activityTime = System.nanoTime() + new Random(System.currentTimeMillis()).nextLong();

                lastLoginTimeMAP.put(accountId, loginTime);
                lastActivityTimeMAP.put(accountId, activityTime);

                //creating entries
                createLastLoginEntry(service, accountId, loginTime);
                updateLastActivityTime(service, accountId, activityTime);
            }

            //Querying!!!
            List<AccountId> query = new ArrayList<AccountId>(accounts);
            query.add(AccountId.generateNew());

            Map<AccountId, Long> loginFromService = service.readLastLogin(query);
            Map<AccountId, Long> activityFromService = service.readLastActivity(query);


            Assert.assertNotNull("loginFromService is NULL", loginFromService);
            Assert.assertNotNull("activityFromService is NULL", activityFromService);

            Assert.assertEquals("Same sizes", loginFromService.size(), activityFromService.size());

            Assert.assertEquals("Same size expected! Cause  only 3 accounts  in the DB", accounts.size(), activityFromService.size());

            for (Map.Entry<AccountId, Long> entry : lastActivityTimeMAP.entrySet()) {
                Assert.assertTrue("From service should contains data", activityFromService.containsKey(entry.getKey()));
                Assert.assertEquals("Data is not equals ! error", entry.getValue(), activityFromService.get(entry.getKey()));
            }

            for (Map.Entry<AccountId, Long> entry : lastLoginTimeMAP.entrySet()) {
                Assert.assertTrue("From service should contains data", loginFromService.containsKey(entry.getKey()));
                Assert.assertEquals("Data is not equals ! error", entry.getValue(), loginFromService.get(entry.getKey()));
            }


            Assert.assertTrue("Only empty map expected!", service.readLastActivity(new ArrayList<AccountId>()).isEmpty());
            Assert.assertTrue("Only empty map expected!", service.readLastLogin(new ArrayList<AccountId>()).isEmpty());


        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
        }


    }

    /**
     * Perform data read on not existing account! to be sure that  exception will be occurred.
     *
     * @param account {@link AccountId}
     * @param service {@link ActivityPersistenceService} instance
     */
    private void checkReadErrors(AccountId account, ActivityPersistenceService service) {
        try {
            service.readLastActivity(account);
            Assert.fail("\"Should fail here! No such data - account should be present!");
        } catch (ActivityPersistenceServiceException e) {
            Assert.assertTrue(e instanceof ActivityNotFoundInPersistenceServiceException);
        }
        try {
            service.readLastLogin(account);
            Assert.fail("Should fail here! No such data - account should be present!");
        } catch (ActivityPersistenceServiceException e) {
            Assert.assertTrue(e instanceof ActivityNotFoundInPersistenceServiceException);
        }
    }

    /**
     * Persist Last login entry to DB!
     *
     * @param service {@link ActivityPersistenceService}
     * @param account {@link AccountId}
     * @param time    time of last loin
     */
    private void createLastLoginEntry(ActivityPersistenceService service, AccountId account, long time) {
        try {
            long timeFromService = service.saveLastLogin(account, time);
            Assert.assertEquals(time, timeFromService);
        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
        }
    }


    /**
     * Updates Last activity entry to DB!
     *
     * @param service {@link ActivityPersistenceService}
     * @param account {@link AccountId}
     * @param time    time of last loin
     */
    private long updateLastActivityTime(ActivityPersistenceService service, AccountId account, long time) {
        try {
            long timeFromService = service.saveLastActivity(account, time);
            Assert.assertEquals(time, timeFromService);
            return timeFromService;
        } catch (ActivityPersistenceServiceException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
