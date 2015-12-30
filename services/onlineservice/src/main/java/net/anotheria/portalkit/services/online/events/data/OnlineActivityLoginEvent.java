package net.anotheria.portalkit.services.online.events.data;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.events.OnlineActivityESOperation;

/**
 * Account logged in event.
 *
 * @author h3llka
 */
public class OnlineActivityLoginEvent extends OnlineActivityEvent {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -6937088725345464965L;
	/**
	 * OnlineActivityLoginEvent 'accountId'.
	 */
	private AccountId accountId;
	/**
	 * OnlineActivityLoginEvent 'lastLoginTime'.
	 */
	private long lastLoginTime;

	/**
	 * Constructor.
	 *
	 * @param account       {@link AccountId}
	 * @param lastLoginTime last login time stamp in nano-seconds
	 */
	public OnlineActivityLoginEvent(final AccountId account, final long lastLoginTime) {
		super(lastLoginTime, OnlineActivityESOperation.ACCOUNT_LOGGED_IN);
		this.accountId = account;
		this.lastLoginTime = lastLoginTime;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	@Override
	public String toString() {
		return "OnlineActivityLoginEvent{" +
				"accountId=" + accountId +
				", lastLoginTime=" + lastLoginTime +
				'}';
	}
}
