package net.anotheria.portalkit.services.online.storage;

import junit.framework.Assert;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.AccountIsOfflineException;
import net.anotheria.portalkit.services.online.AccountIsOnlineException;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.Builder;
import net.anotheria.portalkit.services.online.OnlineServiceConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Junit test for Online user inMemory storage.
 *
 * @author h3llka
 */
public class OnlineStorageTest {

    @BeforeClass
    public static void before() {
        System.setProperty("JUNITTEST", "true");
    }

    @Test
    public void testLoginLogout() {
        OnlineStorage storage = new OnlineStorage();

        final AccountId accountId = AccountId.generateNew();

        Assert.assertFalse("There should not be logged in users", storage.isAccountOnline(accountId));
        Assert.assertEquals("There should not be logged in users", 0, storage.getOnlineUsersAmount());
        Assert.assertEquals("There should not be logged in users", 0, storage.getLastActivityIdxSize());
        Assert.assertEquals("There should not be logged in users", 0, storage.getLastLoginIdxSize());

        //logging in

        long lastLoginTime = System.nanoTime();
        long lastLoginTimeMillis = TimeUnit.MILLISECONDS.transformNanos(lastLoginTime);

        try {
            storage.notifyLoggedIn(accountId, lastLoginTime);
        } catch (AccountIsOnlineException e) {
            Assert.fail("User is already logged in! error!");
        }


        //second call should fail
        try {
            storage.notifyLoggedIn(accountId, System.nanoTime());
            Assert.fail("User is already logged in! 1 step above");
        } catch (AccountIsOnlineException e) {
            //this is it
        }


        // performing check!
        Assert.assertTrue("There should be 1 logged in user", storage.isAccountOnline(accountId));
        Assert.assertEquals("There should  be 1 in users", 1, storage.getOnlineUsersAmount());
        Assert.assertEquals("There should  be 1 logged in users", 1, storage.getLastActivityIdxSize());
        Assert.assertEquals("There should  be 1 logged in users", 1, storage.getLastLoginIdxSize());

        try {
            Assert.assertEquals("Time stamp differs ??? ", lastLoginTimeMillis, storage.getLastLoginTimeStamp(accountId));
        } catch (AccountIsOfflineException e) {
            Assert.fail(e.getMessage());
        }
        try {
            Assert.assertEquals("Time stamp differs ??? ", lastLoginTimeMillis, storage.getLastActivityTimeStamp(accountId));
        } catch (AccountIsOfflineException e) {
            Assert.fail(e.getMessage());
        }


        //LOgging out!

        try {
            storage.notifyLogOut(accountId);
        } catch (AccountIsOfflineException e) {
            Assert.fail("Should not happen! " + e.getMessage());
        }

        //checking that logged out!

        Assert.assertFalse("There should not be logged in users", storage.isAccountOnline(accountId));
        Assert.assertEquals("There should not be logged in users", 0, storage.getOnlineUsersAmount());
        Assert.assertEquals("There should not be logged in users", 0, storage.getLastActivityIdxSize());
        Assert.assertEquals("There should not be logged in users", 0, storage.getLastLoginIdxSize());

        //logging out once again to obtain error
        try {
            storage.notifyLogOut(accountId);
            Assert.fail("Should fail cause noone is logged in");
        } catch (AccountIsOfflineException e) {

        }

    }

    @Test
    public void updateActivityTimeTest() {

        OnlineStorage storage = new OnlineStorage();

        final AccountId accountId = AccountId.generateNew();

        Assert.assertFalse("There should not be logged in users", storage.isAccountOnline(accountId));
        Assert.assertEquals("There should not be logged in users", 0, storage.getOnlineUsersAmount());
        Assert.assertEquals("There should not be logged in users", 0, storage.getLastActivityIdxSize());
        Assert.assertEquals("There should not be logged in users", 0, storage.getLastLoginIdxSize());


        //Updating time since noone is logged in Expecting error
        try {
            storage.notifyActivity(accountId, System.nanoTime());
            Assert.fail("Noone is loged in! Exception should occur!");
        } catch (AccountIsOfflineException e) {
            // this is fine!
        }


        //logging in

        final long lastLoginTime = System.nanoTime();
        final long lastLoginTimeMillis = TimeUnit.MILLISECONDS.transformNanos(lastLoginTime);
        try {
            storage.notifyLoggedIn(accountId, lastLoginTime);
        } catch (AccountIsOnlineException e) {
            Assert.fail("User is already logged in! error!");
        }

        //Should wait  just to check that  activity was updated properly!
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }

        final long lastActivityNanoTime = System.nanoTime();
        final long lastActivityMillis = TimeUnit.MILLISECONDS.transformNanos(lastActivityNanoTime);
        try {
            storage.notifyActivity(accountId, lastActivityNanoTime);
        } catch (AccountIsOfflineException e) {
            Assert.fail(e.getMessage());
        }

        // performing check!
        Assert.assertTrue("There should be 1 logged in user", storage.isAccountOnline(accountId));
        Assert.assertEquals("There should  be 1 in users", 1, storage.getOnlineUsersAmount());
        Assert.assertEquals("There should  be 1 logged in users", 1, storage.getLastActivityIdxSize());
        Assert.assertEquals("There should  be 1 logged in users", 1, storage.getLastLoginIdxSize());

        try {
            Assert.assertEquals("Last login Time stamp differs ??? ", lastLoginTimeMillis, storage.getLastLoginTimeStamp(accountId));
        } catch (AccountIsOfflineException e) {
            Assert.fail(e.getMessage());
        }
        try {
            Assert.assertEquals("Activity Time stamp differs ??? ", lastActivityMillis, storage.getLastActivityTimeStamp(accountId));
        } catch (AccountIsOfflineException e) {
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void invalidArgsTest() {
        OnlineStorage storage = new OnlineStorage();

        try {
            storage.notifyActivity(null, 0);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            storage.notifyLogOut(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            storage.notifyLoggedIn(null, 100L);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            storage.isAccountOnline(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            storage.getLastActivityTimeStamp(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            storage.getLastLoginTimeStamp(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            storage.getLastActivityTimeStamps(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        try {
            storage.getLastLoginTimeStamps(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            storage.readOnlineUsers(null);
            Assert.fail("This is valid argument ???");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

    }

    @Test
    public void testReadLastLoginTimes() {
        List<AccountId> accounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew());

        OnlineStorage storage = new OnlineStorage();
        //checking while noone is logged in


        Map<AccountId, Long> times = storage.getLastLoginTimeStamps(accounts);
        Assert.assertNotNull("Should not be null", times);
        Assert.assertTrue("Empty map expected", times.isEmpty());

        //checking single read!
        for (AccountId acc : accounts)
            try {
                storage.getLastLoginTimeStamp(acc);
                Assert.fail("No such online user found!");
            } catch (AccountIsOfflineException e) {
                //skip
            }


        //logging IN!
        //checking single read!
        long login = System.nanoTime();

        for (AccountId acc : accounts)
            try {
                storage.notifyLoggedIn(acc, login);

            } catch (AccountIsOnlineException e) {
                Assert.fail(e.getMessage());
            }

        Assert.assertEquals(" Time Stamp is not unique ", accounts.size(), storage.getLastActivityIdxSize());
        Assert.assertEquals(" Time Stamp is not unique ", accounts.size(), storage.getLastLoginIdxSize());


        //checking single read!
        for (AccountId acc : accounts)
            try {
                Assert.assertEquals(TimeUnit.MILLISECONDS.transformNanos(login), storage.getLastLoginTimeStamp(acc));
            } catch (AccountIsOfflineException e) {
                Assert.fail();
            }

        //batch read
        times = storage.getLastLoginTimeStamps(accounts);
        Assert.assertNotNull("Should not be null", times);
        Assert.assertFalse("Empty map expected", times.isEmpty());
        //checking batch
        for (Map.Entry<AccountId, Long> entry : times.entrySet())
            Assert.assertEquals(Long.valueOf(TimeUnit.MILLISECONDS.transformNanos(login)), entry.getValue());

    }


    @Test
    public void testReadLastActivityTimes() {
        List<AccountId> accounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew());

        OnlineStorage storage = new OnlineStorage();
        //checking while noone is logged in


        Map<AccountId, Long> times = storage.getLastActivityTimeStamps(accounts);
        Assert.assertNotNull("Should not be null", times);
        Assert.assertTrue("Empty map expected", times.isEmpty());

        //checking single read!
        for (AccountId acc : accounts)
            try {
                storage.getLastActivityTimeStamp(acc);
                Assert.fail("No such online user found!");
            } catch (AccountIsOfflineException e) {
                //skip
            }


        //logging IN!
        //checking single read!
        long login = System.nanoTime();

        for (AccountId acc : accounts)
            try {
                storage.notifyLoggedIn(acc, login);

            } catch (AccountIsOnlineException e) {
                Assert.fail(e.getMessage());
            }

        Assert.assertEquals(" Time Stamp is not unique ", accounts.size(), storage.getLastActivityIdxSize());
        Assert.assertEquals(" Time Stamp is not unique ", accounts.size(), storage.getLastLoginIdxSize());


        //Updating  Activity Time! to be sure!
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Assert.fail("fail@");
        }

        long activityUpdate = System.nanoTime();

        for (AccountId acc : accounts)
            try {
                storage.notifyActivity(acc, activityUpdate);

            } catch (AccountIsOfflineException e) {
                Assert.fail(e.getMessage());
            }

        Assert.assertEquals(" Time Stamp is not unique After activity time update ", accounts.size(), storage.getLastActivityIdxSize());
        Assert.assertEquals(" Time Stamp is not unique After activity time update ", accounts.size(), storage.getLastLoginIdxSize());


        //checking single read!
        for (AccountId acc : accounts)
            try {
                Assert.assertEquals(TimeUnit.MILLISECONDS.transformNanos(activityUpdate), storage.getLastActivityTimeStamp(acc));
            } catch (AccountIsOfflineException e) {
                Assert.fail();
            }

        //batch read
        times = storage.getLastActivityTimeStamps(accounts);
        Assert.assertNotNull("Should not be null", times);
        Assert.assertFalse("Empty map expected", times.isEmpty());
        //checking batch
        for (Map.Entry<AccountId, Long> entry : times.entrySet())
            Assert.assertEquals(Long.valueOf(TimeUnit.MILLISECONDS.transformNanos(activityUpdate)), entry.getValue());

    }

    /**
     * Check read online  - by Account's  without sorting!!!
     */
    @Test
    public void searchCriteriaTest_NONE_SOrt_WITH_ACCOUNTS() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        //create Criteria!
        Builder builder = new Builder();
        builder.withAccounts(activeAccounts);


        List<AccountId> resultList = storage.readOnlineUsers(builder.build());

        Assert.assertNotNull("Accounts NULL!", resultList);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), resultList.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", resultList.contains(account));

    }

    /**
     * Check read online  - by Account's  ASC & DESC  by lastLogin !!!
     */
    @Test
    public void searchCriteriaTest_SORT_BY_LAST_LOGIN_WITH_ACCOUNTS() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        // ####  ASC  read
        //create Criteria!  - ASC  by last login!!!
        Builder builder = new Builder();
        builder.withAccounts(activeAccounts).withASCSortingByLastLoginTime();

        List<AccountId> resultList = storage.readOnlineUsers(builder.build());

        Assert.assertNotNull("Accounts NULL!", resultList);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), resultList.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", resultList.contains(account));

        //Sorting check!!!!

        long previousLAstLogin = 0;
        for (AccountId account : resultList) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLAstLogin != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLAstLogin <= currentValue);

            previousLAstLogin = currentValue;
        }


        // ####  ASC  read  END!

        builder = new Builder();
        builder.withAccounts(activeAccounts).withDESCSortingByLastLoginTime();

        resultList = storage.readOnlineUsers(builder.build());

        Assert.assertNotNull("Accounts NULL!", resultList);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), resultList.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", resultList.contains(account));

        //Sorting check!!!!


        // ####  DESC  read START!
        previousLAstLogin = 0;
        for (AccountId account : resultList) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLAstLogin != 0)
                Assert.assertTrue("Wrong sorting ! DESC  expected but  previous time is less!!!", previousLAstLogin >= currentValue);

            previousLAstLogin = currentValue;
        }


        // ####  DESC  read  END!


    }


    /**
     * Check read online  - by Account's  ASC & DESC  by lastActivity !!!
     */
    @Test
    public void searchCriteriaTest_SORT_BY_LAST_ACTIVITY_WITH_ACCOUNTS() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        // ####  ASC  read
        //create Criteria!  - ASC  by last login!!!
        Builder builder = new Builder();
        builder.withAccounts(activeAccounts).withASCSortingByLastActivityTime();

        List<AccountId> resultList = storage.readOnlineUsers(builder.build());

        Assert.assertNotNull("Accounts NULL!", resultList);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), resultList.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", resultList.contains(account));

        //Sorting check!!!!

        long previousLastActivity = 0;
        for (AccountId account : resultList) {
            long currentValue = lastActivityTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastActivity <= currentValue);

            previousLastActivity = currentValue;
        }


        // ####  ASC  read  END!

        // ####  DESC  read START!

        builder = new Builder();
        builder.withAccounts(activeAccounts).withDESCSortingByLastActivityTime();

        resultList = storage.readOnlineUsers(builder.build());

        Assert.assertNotNull("Accounts NULL!", resultList);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), resultList.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", resultList.contains(account));

        //Sorting check!!!!

        previousLastActivity = 0;
        for (AccountId account : resultList) {
            long currentValue = lastActivityTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! DESC  expected but  previous time is less!!!", previousLastActivity >= currentValue);

            previousLastActivity = currentValue;
        }


        // ####  DESC  read END!


    }

    /**
     * Creating criteria - with time filtering... NONE sorting.
     */
    @Test
    public void timeInterval_onlineUserSearchTest_NONE_SORT() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        //create Criteria!  -
        Builder builder = new Builder();
        builder.withResultAmount(activeAccounts.size());

        List<AccountId> resultList = storage.readOnlineUsers(builder.build());

        Assert.assertNotNull("Accounts NULL!", resultList);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), resultList.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", resultList.contains(account));


        //  change  Result amount!!!
        builder.withResultAmount(1);
        resultList = storage.readOnlineUsers(builder.build());
        Assert.assertEquals("LIMIT query failed!", 1, resultList.size());


    }


    /**
     * Creating criteria - LAST login sorting -  none  time interval providen!
     */
    @Test
    public void timeInterval_onlineUserSearchTest_LAST_LOGIN_SORT_NONE_TIME_INTERVAL() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        // ####  ASC  read
        //create Criteria!  - ASC  by last login!!!
        Builder ascLastLogin = new Builder();
        ascLastLogin.withASCSortingByLastLoginTime();

        List<AccountId> ascLastLoginSorting = storage.readOnlineUsers(ascLastLogin.build());

        Assert.assertNotNull("Accounts NULL!", ascLastLoginSorting);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), ascLastLoginSorting.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", ascLastLoginSorting.contains(account));

        //Sorting check!!!!

        long previousLastLogin = 0;
        for (AccountId account : ascLastLoginSorting) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastLogin != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastLogin <= currentValue);

            previousLastLogin = currentValue;
        }

        // #### ASC END!


        //#### DESC Start
        //create Criteria!  - DESC  by last login!!!
        Builder descLastLogin = new Builder();
        descLastLogin.withDESCSortingByLastActivityTime();

        List<AccountId> descLastLoginSort = storage.readOnlineUsers(descLastLogin.build());

        Assert.assertNotNull("Accounts NULL!", descLastLoginSort);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), descLastLoginSort.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", descLastLoginSort.contains(account));

        //Sorting check!!!!

        previousLastLogin = 0;
        for (AccountId account : descLastLoginSort) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastLogin != 0)
                Assert.assertTrue("Wrong sorting ! DESC  expected but  previous time is less!!!", previousLastLogin >= currentValue);

            previousLastLogin = currentValue;
        }


        //###  DESC end

    }


    /**
     * Creating criteria - LAST activity sorting -  none  time interval providen!
     */
    @Test
    public void timeInterval_onlineUserSearchTest_LAST_ACTIVITY_SORT_NONE_TIME_INTERVAL() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        // ####  ASC  read
        //create Criteria!  - ASC  by last activity!!!
        Builder ascLstActivityQuery = new Builder();
        ascLstActivityQuery.withASCSortingByLastActivityTime();

        List<AccountId> ascLastActivity = storage.readOnlineUsers(ascLstActivityQuery.build());

        Assert.assertNotNull("Accounts NULL!", ascLastActivity);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), ascLastActivity.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", ascLastActivity.contains(account));

        //Sorting check!!!!

        long previousLastActivity = 0;
        for (AccountId account : ascLastActivity) {
            long currentValue = lastActivityTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastActivity <= currentValue);

            previousLastActivity = currentValue;
        }

        // #### ASC END!


        //#### DESC Start

        //create Criteria!  - DESC  by last activity!!!
        Builder descLAstActivityQuery = new Builder();
        descLAstActivityQuery.withDESCSortingByLastActivityTime();

        List<AccountId> descLastLoginSort = storage.readOnlineUsers(descLAstActivityQuery.build());

        Assert.assertNotNull("Accounts NULL!", descLastLoginSort);
        Assert.assertEquals("Wrong result size!", activeAccounts.size(), descLastLoginSort.size());

        //checking each element!
        for (AccountId account : activeAccounts)
            Assert.assertTrue("Required data not found in result", descLastLoginSort.contains(account));

        //Sorting check!!!!

        previousLastActivity = 0;
        for (AccountId account : descLastLoginSort) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! DESC  expected but  previous time is less!!!", previousLastActivity >= currentValue);

            previousLastActivity = currentValue;
        }


        //###  DESC end

    }

    /**
     * Checks all interval timings  for LAstLogin sort type.
     */
    @Test
    public void allTimeInrvals_with_lastLogin_sort_test() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        List<Long> lastLoginTimes = new ArrayList<Long>(lastLoginTimeMap.values());
        //execute Natural sort! ASC  for long entry!
        Collections.sort(lastLoginTimes);
        final long intervalStart = lastLoginTimes.get(0); // first  element
        final long intervalEND = lastLoginTimes.get(lastLoginTimes.size() - 1); // last  element

        // ####  Between
        //create Criteria!  - ASC  by last activity!!!
        Builder betweenLastLoginQuery = new Builder();
        betweenLastLoginQuery.lastLoginBetween(TimeUnit.MILLISECONDS.transformNanos(intervalStart), TimeUnit.MILLISECONDS.transformNanos(intervalEND));

        List<AccountId> betweenQueryResult = storage.readOnlineUsers(betweenLastLoginQuery.build());
        Assert.assertNotNull(" IS NULL! ....", betweenQueryResult);
        for (AccountId result : betweenQueryResult) {
            final Long currentLastLogin = lastLoginTimeMap.get(result);
            long start = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(intervalStart));
            long end = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(intervalEND));
            Assert.assertTrue("Current last login less then interval start ! start[" + start + "] current[" + currentLastLogin + "]", currentLastLogin > start);
            Assert.assertTrue("Current last login greater then interval end ! end[" + intervalEND + "] current[" + currentLastLogin + "]", currentLastLogin < end);
            //Assert.assertTrue(" Result item last login - is out of  interval start/end bound! start[" + intervalStart + "] end[" + intervalEND + "] current[" + currentLastLogin + "] ",
            //      currentLastLogin > intervalStart && currentLastLogin < intervalEND);
        }

        //Check sorting!!
        long previousLastLogin = 0;
        for (AccountId account : betweenQueryResult) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastLogin != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastLogin <= currentValue);

            previousLastLogin = currentValue;
        }
        //# Between end!

        //# AFTER start!!
        final long lessTime = intervalStart;
        Builder afterLAstLoginQuery = new Builder();
        afterLAstLoginQuery.lastLoginGreaterThenTimestamp(TimeUnit.MILLISECONDS.transformNanos(lessTime));

        List<AccountId> accountsLessThen = storage.readOnlineUsers(afterLAstLoginQuery.build());
        Assert.assertNotNull(" IS NULL! ....", accountsLessThen);
        for (AccountId result : accountsLessThen) {
            final Long currentLastLogin = lastLoginTimeMap.get(result);
            long start = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(lessTime));
            Assert.assertTrue(" Result item last login - is out of  interval start/end bound!  ", currentLastLogin > start);
        }
        //Check sorting!!
        previousLastLogin = 0;
        for (AccountId account : accountsLessThen) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastLogin != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastLogin <= currentValue);

            previousLastLogin = currentValue;
        }
        // # After END


        //# BEFORE START
        final long gereaterTime = intervalEND;
        Builder beforeLastLoginQuery = new Builder();
        beforeLastLoginQuery.lastLoginLessThenTimestamp(TimeUnit.MILLISECONDS.transformNanos(gereaterTime));

        List<AccountId> accountsBefore = storage.readOnlineUsers(beforeLastLoginQuery.build());

        beforeLastLoginQuery.withResultAmount(2);
        List<AccountId> accountBeforeLimit2 = storage.readOnlineUsers(beforeLastLoginQuery.build());
        Assert.assertEquals(2, accountBeforeLimit2.size());
        Assert.assertTrue(lastLoginTimeMap.get(accountBeforeLimit2.get(0)) < lastLoginTimeMap.get(accountBeforeLimit2.get(1)));
        Assert.assertTrue(accountsBefore.size() > accountBeforeLimit2.size());
        Assert.assertFalse(accountsBefore.get(0).equals(accountBeforeLimit2.get(0)));


        Assert.assertNotNull(" IS NULL! ....", accountsBefore);
        for (AccountId result : accountsBefore) {
            final Long currentLastLogin = lastLoginTimeMap.get(result);
            long start = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(gereaterTime));
            Assert.assertTrue(" Result item last login - is out of  interval start/end bound!  ", currentLastLogin < start);
        }
        //Check sorting!!
        previousLastLogin = 0;
        for (AccountId account : accountsBefore) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastLogin != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastLogin <= currentValue);

            previousLastLogin = currentValue;
        }

        //# BEFORE END

    }

    /**
     * Checks all interval timings  for LAstLogin sort type.
     */
    @Test
    public void allTimeInrvals_with_lastActivity_sort_test() {
        List<AccountId> activeAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew(), AccountId.generateNew());
        Map<AccountId, Long> lastLoginTimeMap = buildLastLoginData(activeAccounts);
        Map<AccountId, Long> lastActivityTimeMap = buildLastActivityData(lastLoginTimeMap);

        OnlineStorage storage = new OnlineStorage();
        Assert.assertEquals(0, storage.getLastLoginIdxSize());
        Assert.assertEquals(0, storage.getLastActivityIdxSize());

        //Populating test data to the storage!
        loginAccountsForSearchTest(storage, lastLoginTimeMap, lastActivityTimeMap);


        List<Long> lastActivityTimes = new ArrayList<Long>(lastActivityTimeMap.values());
        //execute Natural sort! ASC  for long entry!
        Collections.sort(lastActivityTimes);
        final long intervalStart = lastActivityTimes.get(0); // first  element
        final long intervalEND = lastActivityTimes.get(lastActivityTimes.size() - 1); // last  element

        // ####  Between
        //create Criteria!  - ASC  by last activity!!!
        Builder betweenLastActivityQuery = new Builder();
        betweenLastActivityQuery.lastActivityBetween(TimeUnit.MILLISECONDS.transformNanos(intervalStart), TimeUnit.MILLISECONDS.transformNanos(intervalEND));

        List<AccountId> betweenQueryResult = storage.readOnlineUsers(betweenLastActivityQuery.build());
        Assert.assertNotNull(" IS NULL! ....", betweenQueryResult);
        for (AccountId result : betweenQueryResult) {
            final Long currentLastActivity = lastActivityTimeMap.get(result);
            long start = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(intervalStart));
            long end = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(intervalEND));
            Assert.assertTrue("Current last activity less then interval start ! start[" + start + "] current[" + currentLastActivity + "]", currentLastActivity > start);
            Assert.assertTrue("Current last activity greater then interval end ! end[" + end + "] current[" + currentLastActivity + "]", currentLastActivity < end);
            //Assert.assertTrue(" Result item last login - is out of  interval start/end bound! start[" + intervalStart + "] end[" + intervalEND + "] current[" + currentLastLogin + "] ",
            //      currentLastLogin > intervalStart && currentLastLogin < intervalEND);
        }

        //Check sorting!!
        long previousLastActivity = 0;
        for (AccountId account : betweenQueryResult) {
            long currentValue = lastLoginTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastActivity <= currentValue);

            previousLastActivity = currentValue;
        }
        //# Between end!

        //# AFTER start!!
        final long lessTime = intervalStart;
        Builder afterLastActivityQuery = new Builder();
        afterLastActivityQuery.lastActivityGreaterThenTimestamp(TimeUnit.MILLISECONDS.transformNanos(lessTime));

        List<AccountId> accountsLessThen = storage.readOnlineUsers(afterLastActivityQuery.build());
        Assert.assertNotNull(" IS NULL! ....", accountsLessThen);
        for (AccountId result : accountsLessThen) {
            final Long currentLastLogin = lastActivityTimeMap.get(result);
            long start = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(lessTime));
            Assert.assertTrue(" Result item last login - is out of  interval start/end bound!  ", currentLastLogin >= start);
        }
        //Check sorting!!
        previousLastActivity = 0;
        for (AccountId account : accountsLessThen) {
            long currentValue = lastActivityTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastActivity <= currentValue);

            previousLastActivity = currentValue;
        }
        // # After END


        //# BEFORE START
        final long gereaterTime = intervalEND;
        Builder beforeLastActivityQuery = new Builder();
        beforeLastActivityQuery.lastActivityLessThenTimestamp(TimeUnit.MILLISECONDS.transformNanos(gereaterTime));

        List<AccountId> accountsBefore = storage.readOnlineUsers(beforeLastActivityQuery.build());
        Assert.assertNotNull(" IS NULL! ....", accountsBefore);
        for (AccountId result : accountsBefore) {
            final Long currentLastLogin = lastActivityTimeMap.get(result);
            long start = OnlineStorage.toNanoSeconds(TimeUnit.MILLISECONDS.transformNanos(gereaterTime));
            Assert.assertTrue(" Result item last login - is out of  interval start/end bound!  ", currentLastLogin < start);
        }
        //Check sorting!!
        previousLastActivity = 0;
        for (AccountId account : accountsBefore) {
            long currentValue = lastActivityTimeMap.get(account);

            if (previousLastActivity != 0)
                Assert.assertTrue("Wrong sorting ! ASC  expected but  previous time is greater!!!", previousLastActivity <= currentValue);

            previousLastActivity = currentValue;
        }

        //# BEFORE END

    }


    /**
     * In current test -- for  setup  - few  accounts  with past  time will be  logged in...
     * Afterwards  we will see - if {@link OnlineStorage#cleanUpInactiveAccounts()} works correctly.
     */
    @Test
    public void inactiveAccountsCleanUpTest() {
        OnlineServiceConfiguration config = OnlineServiceConfiguration.getInstance();

        // ### Preparations Start
        final long expiredTimeStamp = System.nanoTime() - (config.getMaxAccountInactivityInterval() * 1000000L);
        List<AccountId> expiredAccounts = Arrays.asList(AccountId.generateNew(), AccountId.generateNew(),
                AccountId.generateNew(), AccountId.generateNew(),
                AccountId.generateNew(), AccountId.generateNew());

        //logging IN
        OnlineStorage storage = new OnlineStorage();

        for (AccountId acc : expiredAccounts)
            try {
                storage.notifyLoggedIn(acc, expiredTimeStamp);
            } catch (AccountIsOnlineException e) {
                Assert.fail("Error!");
            }

        AccountId aliveAcc = AccountId.generateNew();
        try {
            storage.notifyLoggedIn(aliveAcc, System.nanoTime());
        } catch (AccountIsOnlineException e) {
            Assert.fail(e.getMessage());
        }
        // ### Preparations End!

        //executing clean!
        storage.cleanUpInactiveAccounts();

        //now only 1 account should be online!  CHECK!

        for (AccountId acc : expiredAccounts)
            Assert.assertFalse("Expired account is still online! Error", storage.isAccountOnline(acc));

        //LASt acc should be online! !!!
        Assert.assertTrue("Expected to be Online account is Offline! Error", storage.isAccountOnline(aliveAcc));


    }

    /**
     * Creates AccountId to lastLogin mapping.... For Search test.
     *
     * @param accounts {@link AccountId} collection
     * @return Account id to last Login mapping
     */
    private Map<AccountId, Long> buildLastLoginData(List<AccountId> accounts) {
        Map<AccountId, Long> map = new HashMap<AccountId, Long>(accounts.size());
        for (AccountId account : accounts)
            map.put(account, Math.abs(System.nanoTime() + Math.abs(new Random(System.nanoTime()).nextLong()) + 2000000000));

        return map;
    }

    /**
     * Build last activity map for search test.
     *
     * @param lastLogin last login time mapping
     * @return last activity time
     */
    private Map<AccountId, Long> buildLastActivityData(Map<AccountId, Long> lastLogin) {
        Map<AccountId, Long> map = new HashMap<AccountId, Long>(lastLogin.size());
        for (Map.Entry<AccountId, Long> entry : lastLogin.entrySet())
            map.put(entry.getKey(), Math.abs(entry.getValue() + 11000000000L));

        return map;
    }

    /**
     * Simply creates required data inside storage - for  SearchCriteria test.
     * Inside all required asserts will be checked.
     *
     * @param storage          {@link OnlineStorage}
     * @param lastLoginTime    map with the last login time stamps
     * @param lastActivityTime map with the last activity time stamps
     */
    private void loginAccountsForSearchTest(OnlineStorage storage, Map<AccountId, Long> lastLoginTime, Map<AccountId, Long> lastActivityTime) {
        //  first login!
        for (Map.Entry<AccountId, Long> loginEntry : lastLoginTime.entrySet())
            try {
                storage.notifyLoggedIn(loginEntry.getKey(), loginEntry.getValue());
                //check that login performed correctly
                try {
                    Assert.assertEquals("Last login time does not match", TimeUnit.MILLISECONDS.transformNanos(loginEntry.getValue()), storage.getLastLoginTimeStamp(loginEntry.getKey()));
                    Assert.assertEquals("Last activity time does not match", TimeUnit.MILLISECONDS.transformNanos(loginEntry.getValue()), storage.getLastActivityTimeStamp(loginEntry.getKey()));
                } catch (AccountIsOfflineException e) {
                    Assert.fail("Account login failed! " + e.getMessage());
                }


            } catch (AccountIsOnlineException e) {
                Assert.fail("Account should not be online! But it was! " + e.getMessage());
            }


        //NOW  updating last login time!
        //  first login!
        for (Map.Entry<AccountId, Long> activityEntry : lastActivityTime.entrySet())
            try {
                storage.notifyActivity(activityEntry.getKey(), activityEntry.getValue());
                //check that login performed correctly
                try {
                    Assert.assertEquals("Last activity time does not match", TimeUnit.MILLISECONDS.transformNanos(activityEntry.getValue()), storage.getLastActivityTimeStamp(activityEntry.getKey()));
                } catch (AccountIsOfflineException e) {
                    Assert.fail("Account login failed! " + e.getMessage());
                }


            } catch (AccountIsOfflineException e) {
                Assert.fail("Account should  be online! But it was NOT! " + e.getMessage());
            }


    }


}
