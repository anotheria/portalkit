package net.anotheria.portalkit.services.online;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Online users read criteria. Allow to select sort type, sort direction and filter results by preselected {@link AccountId} or some time properties.
 *
 * @author h3llka
 */
public final class OnlineAccountReadCriteria implements Serializable {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 3758880450547497846L;
    /**
     * OnlineAccountReadCriteria 'accounts'.
     */
    private List<AccountId> accounts;
    /**
     * OnlineAccountReadCriteria 'property'.
     */
    private SortProperty property;
    /**
     * OnlineAccountReadCriteria 'ascDirection'.
     */
    private boolean ascDirection;
    /**
     * OnlineAccountReadCriteria 'fromTime', start of the between time interval.
     */
    private long fromTime;
    /**
     * OnlineAccountReadCriteria 'toTime', end of the between time interval.
     */
    private long toTime;
    /**
     * OnlineAccountReadCriteria 'timeStamp', time for After/Before querying.
     */
    private long timeStamp;
    /**
     * OnlineAccountReadCriteria 'timeDirection' to use.
     */
    private TimeBasedQueryDirection timeDirection;
    /**
     * OnlineAccountReadCriteria 'limit'.
     */
    private int limit;

    /**
     * Constructor.
     * {@link IllegalArgumentException} is possible when aProperty or aDirection parameter are not valid.
     *
     * @param aAccounts     {@link AccountId} collection for read
     * @param aProperty     {@link SortProperty} for read
     * @param aAscDirection is asc sort direction required
     * @param aFromTime     between time interval start (millis)
     * @param aToTime       between time interval end (millis)
     * @param aTime         time interval (millis)
     * @param aDirection    time filtering type
     * @param aLimit        expected results amount
     */
    //CHECKSTYLE:OFF
    private OnlineAccountReadCriteria(final List<AccountId> aAccounts, final SortProperty aProperty, final boolean aAscDirection, final long aFromTime, final long aToTime, final long aTime,
                                      final TimeBasedQueryDirection aDirection, int aLimit) {
        if (aProperty == null)
            throw new IllegalArgumentException("invalid aProperty incoming parameter");
        if (aDirection == null)
            throw new IllegalArgumentException("invalid aDirection incoming parameter");
        if (aAccounts == null)
            throw new IllegalArgumentException("wrong aAccounts argument passed");
        if (aLimit <= 0)
            throw new IllegalArgumentException("wrong aLimit parameter passed");
        if (aFromTime != 0 && aTime != 0 && aFromTime >= aToTime)
            throw new IllegalArgumentException("wrong time range passed! interval start should be less than interval end. start[" + aFromTime + "], end[" + aToTime + "], check aFromTime & aToTime parameters");

        this.accounts = aAccounts;
        this.property = aProperty;
        this.ascDirection = aAscDirection;
        this.fromTime = aFromTime;
        this.toTime = aToTime;
        this.timeStamp = aTime;
        this.timeDirection = aDirection;
        this.limit = aLimit;
    }

    public List<AccountId> getAccounts() {
        return accounts;
    }

    public SortProperty getProperty() {
        return property;
    }

    public boolean isAscDirection() {
        return ascDirection;
    }

    public long getFromTime() {
        return fromTime;
    }


    public long getToTime() {
        return toTime;
    }


    public long getTimeStamp() {
        return timeStamp;
    }


    public TimeBasedQueryDirection getTimeDirection() {
        return timeDirection;
    }


    public int getLimit() {
        return limit;
    }


    @Override
    public String toString() {
        return "OnlineAccountReadCriteria{" +
                "accounts=" + accounts +
                ", property=" + property +
                ", ascDirection=" + ascDirection +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", timeStamp=" + timeStamp +
                ", timeDirection=" + timeDirection +
                ", limit=" + limit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnlineAccountReadCriteria criteria = (OnlineAccountReadCriteria) o;

        if (ascDirection != criteria.ascDirection) return false;
        if (fromTime != criteria.fromTime) return false;
        if (limit != criteria.limit) return false;
        if (timeStamp != criteria.timeStamp) return false;
        if (toTime != criteria.toTime) return false;
        if (accounts != null ? !accounts.equals(criteria.accounts) : criteria.accounts != null) return false;
        if (property != criteria.property) return false;
        if (timeDirection != criteria.timeDirection) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accounts != null ? accounts.hashCode() : 0;
        result = 31 * result + (property != null ? property.hashCode() : 0);
        result = 31 * result + (ascDirection ? 1 : 0);
        result = 31 * result + (int) (fromTime ^ (fromTime >>> 32));
        result = 31 * result + (int) (toTime ^ (toTime >>> 32));
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        result = 31 * result + (timeDirection != null ? timeDirection.hashCode() : 0);
        result = 31 * result + limit;
        return result;
    }

    /**
     * Builder for {@link OnlineAccountReadCriteria}.
     */
    public static class Builder {
        /**
         * Default result limit.
         */
        private static final int DEFAULT_LIMIT = 1000;
        /**
         * Builder 'accounts'. With default init.
         */
        private List<AccountId> accounts = new ArrayList<AccountId>();
        /**
         * Builder 'property'. With default init.
         * Disabled by default.
         */
        private SortProperty property = SortProperty.NONE;
        /**
         * Builder 'ascDirection'. With default init.
         */
        private boolean ascDirection = true;
        /**
         * Time interval start.
         */
        private long fromTime;
        /**
         * Time interval end.
         */
        private long toTime;
        /**
         * Time stamp for querying.
         */
        private long timeStamp;
        /**
         * Result limit with default init.
         */
        private int limit = DEFAULT_LIMIT;
        /**
         * TimeBasedQueryDirection  direction of querying by time stamp.
         * NONE - by default.
         */
        private TimeBasedQueryDirection timeDirection = TimeBasedQueryDirection.NONE;


        /**
         * Use {@link AccountId} collection for further {@link OnlineAccountReadCriteria} build.
         * {@link IllegalArgumentException} in case of {@code null} incoming data, or in case when collection isEmpty.
         * NOTE : account collection can't be used both with TimeQuerying!
         * Using {@link AccountId} collection - will also decrease limit to collection size.
         *
         * @param aAccounts {@link AccountId} collection
         * @return {@link Builder}
         */
        public Builder withAccounts(final List<AccountId> aAccounts) {
            if (aAccounts == null || aAccounts.isEmpty())
                throw new IllegalArgumentException("accounts parameter is not valid");
            this.accounts = aAccounts;
            this.limit = aAccounts.size();

            this.timeDirection = TimeBasedQueryDirection.NONE;
            this.fromTime = 0;
            this.toTime = 0;
            this.timeStamp = 0;
            return this;
        }


        /**
         * Enable asc sorting by user last login time.
         * Filter should be used both with {@link AccountId} collection.
         * There is no need to use such with TimeQuerying.
         *
         * @return {@link Builder}
         */
        public Builder withASCSortingByLastLoginTime() {
            this.property = SortProperty.LAST_LOGIN;
            this.ascDirection = true;
            return this;
        }

        /**
         * Enable desc sorting by user last login time.
         * Filter should be used both with {@link AccountId} collection.
         * There is no need to use such with TimeQuerying.
         *
         * @return {@link Builder}
         */
        public Builder withDESCSortingByLastLoginTime() {
            this.property = SortProperty.LAST_LOGIN;
            this.ascDirection = false;
            return this;
        }


        /**
         * Enable asc sorting by user last activity time.
         * Filter should be used both with {@link AccountId} collection.
         * There is no need to use such with TimeQuerying.
         *
         * @return {@link Builder}
         */
        public Builder withASCSortingByLastActivityTime() {
            this.property = SortProperty.LAST_ACTIVITY;
            this.ascDirection = true;
            return this;
        }

        /**
         * Enable desc sorting by user last activity time.
         * Filter should be used both with {@link AccountId} collection.
         * There is no need to use such with TimeQuerying.
         *
         * @return {@link Builder}
         */
        public Builder withDESCSortingByLastActivityTime() {
            this.property = SortProperty.LAST_ACTIVITY;
            this.ascDirection = false;
            return this;
        }


        /**
         * For selecting only data - where time interval is greater then selected. Sorting by {@link SortProperty#LAST_LOGIN}.
         * NOTE : Time querying can't be used both with {@link AccountId} collection filtering. Previously set collection data will be reset, limit will be reset to defaults - {@link Builder#DEFAULT_LIMIT}.
         *
         * @param timestamp long time
         * @return {@link Builder}
         */
        public Builder lastLoginGreaterThenTimestamp(final long timestamp) {
            this.toTime = 0L;
            this.fromTime = 0L;
            this.timeStamp = timestamp;
            this.timeDirection = TimeBasedQueryDirection.AFTER;
            this.property = SortProperty.LAST_LOGIN;
            this.ascDirection = true;

            //should cleanup up in case if before AccountIds were passed
            resetLimitAndCleanAccountIds();
            return this;
        }

        /**
         * For selecting only data - where time interval is less then selected. Sorting by {@link SortProperty#LAST_LOGIN}.
         * NOTE : Time querying can't be used both with {@link AccountId} collection filtering. Previously set collection data will be reset, limit will be reset to defaults - {@link Builder#DEFAULT_LIMIT}.
         *
         * @param timestamp long time
         * @return {@link Builder}
         */
        public Builder lastLoginLessThenTimestamp(final long timestamp) {
            this.toTime = 0L;
            this.fromTime = 0L;
            this.timeStamp = timestamp;
            this.timeDirection = TimeBasedQueryDirection.BEFORE;
            this.property = SortProperty.LAST_LOGIN;
            this.ascDirection = true;

            //should cleanup up in case if before AccountIds were passed
            resetLimitAndCleanAccountIds();
            return this;
        }

        /**
         * For selecting only data - where time interval is inside of the selected one. Sorting by {@link SortProperty#LAST_LOGIN}.
         * NOTE : Time querying can't be used both with {@link AccountId} collection filtering. Previously set collection data will be reset, limit will be reset to defaults - {@link Builder#DEFAULT_LIMIT}.
         * {@link IllegalArgumentException} appears in case if interval start greater equals than interval end.
         *
         * @param startTime interval start
         * @param endTime   interval end
         * @return {@link Builder}
         */
        public Builder lastLoginBetween(final long startTime, final long endTime) {
            if (startTime >= endTime)
                throw new IllegalArgumentException("interval start[" + startTime + "] should be greater that end[" + endTime + "]");

            this.fromTime = startTime;
            this.toTime = endTime;
            this.timeStamp = 0L;
            this.timeDirection = TimeBasedQueryDirection.BETWEEN;
            this.property = SortProperty.LAST_LOGIN;
            this.ascDirection = true;

            resetLimitAndCleanAccountIds();
            return this;
        }


        /**
         * For selecting only data - where time interval is greater then selected. Sorting by {@link SortProperty#LAST_ACTIVITY}.
         * NOTE : Time querying can't be used both with {@link AccountId} collection filtering. Previously set collection data will be reset, limit will be reset to defaults - {@link Builder#DEFAULT_LIMIT}.
         *
         * @param timestamp long time
         * @return {@link Builder}
         */
        public Builder lastActivityGreaterThenTimestamp(final long timestamp) {
            this.toTime = 0L;
            this.fromTime = 0L;
            this.timeStamp = timestamp;
            this.timeDirection = TimeBasedQueryDirection.AFTER;
            this.property = SortProperty.LAST_ACTIVITY;
            this.ascDirection = true;

            //should cleanup up in case if before AccountIds were passed
            resetLimitAndCleanAccountIds();
            return this;
        }

        /**
         * For selecting only data - where time interval is less then selected. Sorting by {@link SortProperty#LAST_ACTIVITY}.
         * NOTE : Time querying can't be used both with {@link AccountId} collection filtering. Previously set collection data will be reset, limit will be reset to defaults - {@link Builder#DEFAULT_LIMIT}.
         *
         * @param timestamp long time
         * @return {@link Builder}
         */
        public Builder lastActivityLessThenTimestamp(final long timestamp) {
            this.toTime = 0L;
            this.fromTime = 0L;
            this.timeStamp = timestamp;
            this.timeDirection = TimeBasedQueryDirection.BEFORE;
            this.property = SortProperty.LAST_ACTIVITY;
            this.ascDirection = true;

            //should cleanup up in case if before AccountIds were passed
            resetLimitAndCleanAccountIds();
            return this;
        }

        /**
         * For selecting only data - where time interval is inside of the selected one. Sorting by {@link SortProperty#LAST_ACTIVITY}.
         * NOTE : Time querying can't be used both with {@link AccountId} collection filtering. Previously set collection data will be reset, limit will be reset to defaults - {@link Builder#DEFAULT_LIMIT}.
         * {@link IllegalArgumentException} appears in case if interval start greater equals than interval end.
         *
         * @param startTime interval start
         * @param endTime   interval end
         * @return {@link Builder}
         */
        public Builder lastActivityBetween(final long startTime, final long endTime) {
            if (startTime >= endTime)
                throw new IllegalArgumentException("interval start[" + startTime + "] should be greater that end[" + endTime + "]");

            this.fromTime = startTime;
            this.toTime = endTime;
            this.timeStamp = 0L;
            this.timeDirection = TimeBasedQueryDirection.BETWEEN;
            this.property = SortProperty.LAST_ACTIVITY;
            this.ascDirection = true;

            resetLimitAndCleanAccountIds();
            return this;
        }

        /**
         * Allow to provide search result limit.
         * NOTE - in case of building query with {@link AccountId} collection - limit will be automatically changed to collection size.
         * {@link IllegalArgumentException} appears in case if limit is less that 1.
         *
         * @param aLimit limit to use
         * @return {@link Builder}
         */
        public Builder withResultAmount(final int aLimit) {
            if (aLimit <= 0)
                throw new IllegalArgumentException("invalid limit parameter found. Positive value greater than 0 expected");
            this.limit = aLimit;
            return this;
        }

        /**
         * Reset limit to defaults, clear {@link AccountId} collection, in case of TimeQuerying.
         */
        private void resetLimitAndCleanAccountIds() {
            if (accounts != null) {
                accounts.clear();
                limit = DEFAULT_LIMIT;
            }
        }

        /**
         * Build {@link OnlineAccountReadCriteria}.
         *
         * @return {@link OnlineAccountReadCriteria}
         */
        public OnlineAccountReadCriteria build() {
            return new OnlineAccountReadCriteria(accounts, property, ascDirection, fromTime, toTime, timeStamp, timeDirection, limit);
        }


    }

    /**
     * Possible sort-by properties.
     * For {@link OnlineService} there are 2 possible sort type's. lastLogin, lastActivity.
     * NONE - constant should be used by defaults.
     */
    public static enum SortProperty {
        /**
         * Sorting is disabled.
         */
        NONE,
        /**
         * Sort by last-login.
         */
        LAST_LOGIN,
        /**
         * Sort by last-activity.
         */
        LAST_ACTIVITY
    }

    /**
     * Possible time directions, for additional querying by provided time stamps.
     * Allow to execute -- read data before timestamp, after time stamp, between two time stamps.
     * Should be disabled by default - using NONE constant.
     */
    public static enum TimeBasedQueryDirection {
        /**
         * Default, without time filtering.
         */
        NONE,
        /**
         * Before some timeStamp.
         */
        BEFORE,
        /**
         * After some timestamp.
         */
        AFTER,
        /**
         * Between 2 time stamps. From interval start - to interval end.
         */
        BETWEEN
    }

}
