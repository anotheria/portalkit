package net.anotheria.portalkit.services.online;

import junit.framework.Assert;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.Builder;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.SortProperty;
import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria.TimeBasedQueryDirection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Junit test.
 *
 * @author h3llka
 */
public class OnlineAccountReadCriteriaTest {


    @Test
    public void criteriaDefaultBuildTest() {
        Builder criteriaBuilder = new Builder();
        OnlineAccountReadCriteria criteria = criteriaBuilder.build();
        Assert.assertNotNull(criteria);

        Assert.assertEquals("Wrong timing ", TimeBasedQueryDirection.NONE, criteria.getTimeDirection());
        Assert.assertEquals("Wrong timing. Timestamp not set ", 0, criteria.getTimeStamp());
        Assert.assertEquals("Wrong timing . To time not set", 0, criteria.getToTime());
        Assert.assertEquals("Wrong timing . From Time not set", 0, criteria.getFromTime());
        Assert.assertTrue("Default is ASC", criteria.isAscDirection());
        Assert.assertEquals("Default is NONE", SortProperty.NONE, criteria.getProperty());
        Assert.assertTrue("Default is empty", criteria.getAccounts().isEmpty());
        Assert.assertTrue("Default is 1000", criteria.getLimit() != 0);

    }


    @Test
    public void criteriaWithAccountsAndSorting() {
        Builder criteriaBuilder = new Builder();
        criteriaBuilder.withAccounts(Arrays.asList(AccountId.generateNew()));

        OnlineAccountReadCriteria criteria = criteriaBuilder.build();
        Assert.assertNotNull(criteria);

        Assert.assertEquals("Wrong timing ", TimeBasedQueryDirection.NONE, criteria.getTimeDirection());
        Assert.assertEquals("Wrong timing. Timestamp not set ", 0, criteria.getTimeStamp());
        Assert.assertEquals("Wrong timing . To time not set", 0, criteria.getToTime());
        Assert.assertEquals("Wrong timing . From Time not set", 0, criteria.getFromTime());
        Assert.assertTrue("Default is ASC", criteria.isAscDirection());
        Assert.assertEquals("Default is NONE", SortProperty.NONE, criteria.getProperty());
        Assert.assertFalse("Should be i element", criteria.getAccounts().isEmpty());
        Assert.assertEquals("Default is 1000", criteria.getAccounts().size(), criteria.getLimit());


        //with accounts and sorting! LastLogin ASC
        criteriaBuilder.withASCSortingByLastLoginTime();
        criteria = criteriaBuilder.build();
        Assert.assertEquals("LAst login! as Specified ! failed", SortProperty.LAST_LOGIN, criteria.getProperty());

        //with accounts and sorting! LastLogin DESC
        criteriaBuilder.withDESCSortingByLastLoginTime();
        criteria = criteriaBuilder.build();
        Assert.assertEquals("LAst login! as Specified ! failed", SortProperty.LAST_LOGIN, criteria.getProperty());
        Assert.assertFalse("Default is ASC, but DESC is specified", criteria.isAscDirection());


        //with accounts and sorting! LAstActivity ASC
        criteriaBuilder.withASCSortingByLastActivityTime();
        criteria = criteriaBuilder.build();
        Assert.assertEquals("LAst login! as Specified ! failed", SortProperty.LAST_ACTIVITY, criteria.getProperty());

        //with accounts and sorting! LastLogin DESC
        criteriaBuilder.withDESCSortingByLastActivityTime();
        criteria = criteriaBuilder.build();
        Assert.assertEquals("LAst login! as Specified ! failed", SortProperty.LAST_ACTIVITY, criteria.getProperty());
        Assert.assertFalse("Default is ASC, but DESC is specified", criteria.isAscDirection());

        try {
            criteriaBuilder.withAccounts(null);
            Assert.fail("Illegal collection !");
        } catch (IllegalArgumentException e) {
        }
        try {
            criteriaBuilder.withAccounts(new ArrayList<AccountId>());
            Assert.fail("Illegal collection !");
        } catch (IllegalArgumentException e) {
        }

        try {
            criteriaBuilder.withResultAmount(0);
            Assert.fail("Illegal value! Greater than 0 expected !");
        } catch (IllegalArgumentException e) {
        }


    }


    @Test
    public void criteriaWithTimingProperties() {
        Builder criteriaBuilder = new Builder();
        criteriaBuilder.lastActivityLessThenTimestamp(100L);

        OnlineAccountReadCriteria criteria = criteriaBuilder.build();
        Assert.assertNotNull(criteria);

        Assert.assertEquals("Wrong timing Before specified", TimeBasedQueryDirection.BEFORE, criteria.getTimeDirection());
        Assert.assertEquals("Wrong timing. Timestamp 100L set ", 100L, criteria.getTimeStamp());
        Assert.assertEquals("Wrong timing . To time not set", 0, criteria.getToTime());
        Assert.assertEquals("Wrong timing . From Time not set", 0, criteria.getFromTime());
        Assert.assertTrue("Default is ASC", criteria.isAscDirection());
        Assert.assertEquals("Default is NONE, LAST_ACTIVITY specified ", SortProperty.LAST_ACTIVITY, criteria.getProperty());
        Assert.assertTrue("Should be no elements", criteria.getAccounts().isEmpty());

        criteriaBuilder.lastActivityBetween(100L, 200L);
        criteria = criteriaBuilder.build();
        Assert.assertEquals("Wrong timing Before specified", TimeBasedQueryDirection.BETWEEN, criteria.getTimeDirection());
        Assert.assertEquals("Default is NONE, LAST_ACTIVITY specified ", SortProperty.LAST_ACTIVITY, criteria.getProperty());
        Assert.assertEquals("Wrong timing .", 200L, criteria.getToTime());
        Assert.assertEquals("Wrong timing .", 100L, criteria.getFromTime());

        criteriaBuilder.lastActivityGreaterThenTimestamp(100L);

        criteria = criteriaBuilder.build();
        Assert.assertNotNull(criteria);
        Assert.assertEquals("Wrong timing .", 0, criteria.getToTime());
        Assert.assertEquals("Wrong timing .", 0, criteria.getFromTime());
        Assert.assertEquals("Wrong timing Before specified", TimeBasedQueryDirection.AFTER, criteria.getTimeDirection());
        Assert.assertEquals("Default is NONE, LAST_ACTIVITY specified ", SortProperty.LAST_ACTIVITY, criteria.getProperty());


        try {
            criteriaBuilder.lastActivityBetween(0, -100);
            Assert.fail("Not valid range");
        } catch (IllegalArgumentException e) {
        }
        try {
            criteriaBuilder.lastLoginBetween(0, -100);
            Assert.fail("Not valid range");
        } catch (IllegalArgumentException e) {
        }


        criteriaBuilder.lastLoginBetween(100L, 200L);
        criteria = criteriaBuilder.build();
        Assert.assertEquals("Wrong timing Before specified", TimeBasedQueryDirection.BETWEEN, criteria.getTimeDirection());
        Assert.assertEquals("Default is NONE, LAST_LOGIN specified ", SortProperty.LAST_LOGIN, criteria.getProperty());
        Assert.assertEquals("Wrong timing .", 200L, criteria.getToTime());
        Assert.assertEquals("Wrong timing .", 100L, criteria.getFromTime());


        criteriaBuilder.lastLoginLessThenTimestamp(100L);
        criteria = criteriaBuilder.build();
        Assert.assertEquals("Wrong timing Before specified", TimeBasedQueryDirection.BEFORE, criteria.getTimeDirection());
        Assert.assertEquals("Default is NONE, LAST_LOGIN specified ", SortProperty.LAST_LOGIN, criteria.getProperty());
        Assert.assertEquals("Wrong timing .", 0, criteria.getToTime());
        Assert.assertEquals("Wrong timing .", 0, criteria.getFromTime());
        Assert.assertEquals("Wrong timing .", 100, criteria.getTimeStamp());

        criteriaBuilder.lastLoginGreaterThenTimestamp(100L);
        criteria = criteriaBuilder.build();
        Assert.assertEquals("Wrong timing Before specified", TimeBasedQueryDirection.AFTER, criteria.getTimeDirection());
        Assert.assertEquals("Default is NONE, LAST_LOGIN specified ", SortProperty.LAST_LOGIN, criteria.getProperty());
        Assert.assertEquals("Wrong timing .", 0, criteria.getToTime());
        Assert.assertEquals("Wrong timing .", 0, criteria.getFromTime());
        Assert.assertEquals("Wrong timing .", 100, criteria.getTimeStamp());


    }

}
