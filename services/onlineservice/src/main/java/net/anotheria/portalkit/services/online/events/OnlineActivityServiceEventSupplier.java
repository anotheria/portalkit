package net.anotheria.portalkit.services.online.events;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.eventing.AbstractServiceEventSupplier;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityEvent;

import java.util.List;

/**
 * {@link OnlineActivityEvent} announcer.
 *
 * @author h3llka
 */
public class OnlineActivityServiceEventSupplier extends AbstractServiceEventSupplier {

	/**
	 * Channel name.
	 */
	public static final String EVENT_CHANNEL_NAME = "OnlineActivityEventChannel";


	/**
	 * Constructor.
	 */
	public OnlineActivityServiceEventSupplier() {
		super("OnlineActivityServiceEventSupplier", EVENT_CHANNEL_NAME);
	}

	/**
	 * Send {@link net.anotheria.portalkit.services.online.events.data.OnlineActivityLoginEvent}.
	 *
	 * @param account       {@link AccountId}
	 * @param lastLoginTime last login time stamp
	 */
	public void accountLoggedIn(final AccountId account, final long lastLoginTime) {
		send(OnlineActivityEvent.login(account, lastLoginTime));
	}

	/**
	 * Send {@link net.anotheria.portalkit.services.online.events.data.OnlineActivityUpdateEvent}.
	 *
	 * @param account          {@link AccountId}
	 * @param lastActivityTime last activity time stamp
	 */
	public void accountActivityChange(final AccountId account, final long lastActivityTime) {
		send(OnlineActivityEvent.activityUpdate(account, lastActivityTime));
	}

	/**
	 * Send {@link net.anotheria.portalkit.services.online.events.data.OnlineActivityLogoutEvent}.
	 *
	 * @param account    {@link AccountId}
	 * @param logOutTime time in nano-seconds when logOut was performed
	 */
	public void accountLoggedOut(final AccountId account, final long logOutTime) {
		send(OnlineActivityEvent.logOut(account, logOutTime));
	}

	/**
	 * Send {@link net.anotheria.portalkit.services.online.events.data.OnlineActivityCleanUpEvent}.
	 *
	 * @param accounts {@link AccountId} collection
	 */
	public void cleanUp(final List<AccountId> accounts) {
		send(OnlineActivityEvent.cleanUp(accounts));
	}

}
