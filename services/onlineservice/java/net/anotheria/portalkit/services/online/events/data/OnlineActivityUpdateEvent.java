package net.anotheria.portalkit.services.online.events.data;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.events.OnlineActivityESOperation;

/**
 * Activity update event.
 *
 * @author h3llka
 */
public class OnlineActivityUpdateEvent extends OnlineActivityEvent {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -7482183759872964473L;
	/**
	 * OnlineActivityLoginEvent 'accountId'.
	 */
	private AccountId accountId;
	/**
	 * OnlineActivityLoginEvent 'lastLoginTime'.
	 */
	private long lastActivityTime;

	/**
	 * Constructor.
	 *
	 * @param account          {@link AccountId}
	 * @param lastActivityTime last activity update time
	 */
	public OnlineActivityUpdateEvent(final AccountId account, final long lastActivityTime) {
		super(lastActivityTime, OnlineActivityESOperation.ACCOUNT_ACTIVITY_UPDATE);
		this.accountId = account;
		this.lastActivityTime = lastActivityTime;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public long getLastActivityTime() {
		return lastActivityTime;
	}

	@Override
	public String toString() {
		return "OnlineActivityUpdateEvent{" +
				"accountId=" + accountId +
				", lastActivityTime=" + lastActivityTime +
				'}';
	}
}
