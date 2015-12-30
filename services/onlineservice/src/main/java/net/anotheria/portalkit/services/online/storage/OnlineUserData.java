package net.anotheria.portalkit.services.online.storage;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;

/**
 * Represents basic data for storing online user stuff.
 * Required - that all timestamps were passed as Nano-time!
 *
 * @author h3llka
 */
public class OnlineUserData implements Serializable {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 3733177152425352433L;
    /**
     * OnlineUserData 'accountId'.
     */
    private AccountId accountId;
    /**
     * OnlineUserData 'lastLoginNanoTime'.
     * Timestamp of last login in nano seconds.
     */
    private long lastLoginNanoTime;
    /**
     * OnlineUserData 'lastActivityNanoTime'.
     * Timestamp of last activity in nano seconds.
     */
    private long lastActivityNanoTime;

    /**
     * Constructor.
     *
     * @param accountId {@link AccountId}
     */
    public OnlineUserData(final AccountId accountId) {
        this.accountId = accountId;
    }

    /**
     * Constructor.
     *
     * @param accountId            {@link AccountId}
     * @param lastLoginNanoTime    last login time
     * @param lastActivityNanoTime last activity time
     */
    public OnlineUserData(final AccountId accountId, final long lastLoginNanoTime, final long lastActivityNanoTime) {
        this.accountId = accountId;
        this.lastLoginNanoTime = lastLoginNanoTime;
        this.lastActivityNanoTime = lastActivityNanoTime;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public long getLastLoginNanoTime() {
        return lastLoginNanoTime;
    }

    public void setLastLoginNanoTime(long lastLoginNanoTime) {
        this.lastLoginNanoTime = lastLoginNanoTime;
    }

    public long getLastActivityNanoTime() {
        return lastActivityNanoTime;
    }

    public void setLastActivityNanoTime(long lastActivityNanoTime) {
        this.lastActivityNanoTime = lastActivityNanoTime;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OnlineUserData && OnlineUserData.class.cast(o).getAccountId().equals(getAccountId());
    }

    @Override
    public int hashCode() {
        return accountId != null ? accountId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OnlineUserData{" +
                "accountId=" + accountId +
                ", lastLoginNanoTime=" + lastLoginNanoTime +
                ", lastActivityNanoTime=" + lastActivityNanoTime +
                '}';
    }
}
