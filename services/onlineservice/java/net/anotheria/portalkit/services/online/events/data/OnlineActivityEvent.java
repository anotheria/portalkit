package net.anotheria.portalkit.services.online.events.data;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import net.anotheria.portalkit.services.online.events.OnlineActivityESOperation;

import java.util.List;

/**
 * Represent basic event for {@link net.anotheria.portalkit.services.online.OnlineService}.
 *
 * @author h3llka
 */
public abstract class OnlineActivityEvent extends ServiceEventData {
	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 6605162683331419396L;

	/**
	 * OnlineActivityEvent operation type.
	 */
	private OnlineActivityESOperation operation;
	/**
	 * OnlineActivityEvent eventTime.
	 * Always has event time in nanoSeconds from server side.
	 */
	private long eventTime;

	/**
	 * Constructor.
	 *
	 * @param timeStamp nanoTime - of the event creation
	 * @param operation {@link OnlineActivityESOperation}
	 */
	protected OnlineActivityEvent(final long timeStamp, final OnlineActivityESOperation operation) {
		this.operation = operation;
		this.eventTime = timeStamp;
	}

	public OnlineActivityESOperation getOperation() {
		return operation;
	}

	public long getEventTime() {
		return eventTime;
	}

	@Override
	public String getOperationType() {
		return operation.getValue();
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
	public static OnlineActivityEvent logOut(final AccountId account, final long logoutTime) {
		return new OnlineActivityLogoutEvent(account, logoutTime);
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
