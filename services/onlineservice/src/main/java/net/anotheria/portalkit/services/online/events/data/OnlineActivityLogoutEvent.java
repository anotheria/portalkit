package net.anotheria.portalkit.services.online.events.data;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.online.events.OnlineActivityESOperation;

/**
 * Account logged out activity event.
 *
 * @author h3llka
 */
public class OnlineActivityLogoutEvent extends OnlineActivityEvent {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -2836440306504689765L;
	/**
	 * OnlineActivityLoginEvent 'accountId'.
	 */
	private AccountId accountId;

	/**
	 * Constructor.
	 *
	 * @param account {@link AccountId}
	 * @param logoutNanoTime time in nano-seconds when operation was performed
	 */
	public OnlineActivityLogoutEvent(final AccountId account, final long logoutNanoTime) {
		super(logoutNanoTime, OnlineActivityESOperation.ACCOUNT_LOGGED_OUT);
		this.accountId = account;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	@Override
	public String toString() {
		return "OnlineActivityLogoutEvent{" +
				"accountId=" + accountId +
				'}';
	}
}
