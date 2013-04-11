package net.anotheria.portalkit.services.online.events;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.List;

/**
 * Represent basic event for {@link net.anotheria.portalkit.services.online.OnlineService}.
 *
 * @author h3llka
 */
public abstract class OnlineActivityEvent implements Serializable {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 9067944511994359461L;
    /**
     * OnlineActivityEvent operation type.
     */
    private OnlineActivityESOperation operation;

    /**
     * Constructor.
     *
     * @param operation {@link OnlineActivityESOperation}
     */
    protected OnlineActivityEvent(OnlineActivityESOperation operation) {
        this.operation = operation;
    }

    public OnlineActivityESOperation getOperation() {
        return operation;
    }

    /**
     * Create {@link OnlineActivityLoginEvent}.
     *
     * @param account       {@link AccountId}
     * @param lastLoginTime last login time stamp
     * @return {@link OnlineActivityLoginEvent}
     */
    public static OnlineActivityEvent login(final AccountId account, final long lastLoginTime) {
        return new OnlineActivityLoginEvent(account, lastLoginTime);
    }

    /**
     * Create {@link OnlineActivityUpdateEvent}.
     *
     * @param account          {@link AccountId}
     * @param lastActivityTime last activity time stamp
     * @return {@link OnlineActivityUpdateEvent}
     */
    public static OnlineActivityEvent activityUpdate(final AccountId account, final long lastActivityTime) {
        return new OnlineActivityUpdateEvent(account, lastActivityTime);
    }

    /**
     * Create {@link OnlineActivityLogoutEvent}.
     *
     * @param account {@link AccountId}
     * @return {@link OnlineActivityLogoutEvent}
     */
    public static OnlineActivityEvent logOut(final AccountId account) {
        return new OnlineActivityLogoutEvent(account);
    }

    /**
     * Create {@link OnlineActivityCleanUpEvent}.
     *
     * @param accounts {@link AccountId} collection (accounts which were cleaned  up)
     * @return {@link OnlineActivityCleanUpEvent}
     */
    public static OnlineActivityEvent cleanUp(final List<AccountId> accounts) {
        return new OnlineActivityCleanUpEvent(accounts);
    }
}
