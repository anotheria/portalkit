package net.anotheria.portalkit.services.account.event;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.event.data.AccountCreateEventData;
import net.anotheria.portalkit.services.account.event.data.AccountDeleteEventData;
import net.anotheria.portalkit.services.account.event.data.AccountUpdateEventData;
import net.anotheria.portalkit.services.common.eventing.AbstractServiceEventSupplier;

/**
 * AccountService event supplier.
 *
 * @author Alex Osadchy
 */
public class AccountServiceEventSupplier extends AbstractServiceEventSupplier {

	/**
	 * Channel name.
	 */
	public static final String CHANNEL_NAME = "AccountServiceEventChannel";

	/**
	 * Default constructor.
	 */
	public AccountServiceEventSupplier() {
		super("AccountServiceEventSupplier", CHANNEL_NAME);
	}

	/**
	 * Sends event about created {@link Account}.
	 *
	 * @param account created {@link Account}
	 */
	public void accountCreated(final Account account) {
		if (account == null)
			throw new IllegalArgumentException("account is null");

		send(new AccountCreateEventData(account));
	}

	/**
	 * Sends event about updated {@link Account}.
	 *
	 * @param beforeUpdate {@link Account} before update
	 * @param afterUpdate {@link Account} after update
	 */
	public void accountUpdated(final Account beforeUpdate, final Account afterUpdate) {
		if (beforeUpdate == null)
			throw new IllegalArgumentException("beforeUpdate is null");
		if (afterUpdate == null)
			throw new IllegalArgumentException("afterUpdate is null");

		send(new AccountUpdateEventData(beforeUpdate, afterUpdate));
	}

	/**
	 * Sends event about deleted {@link Account}.
	 *
	 * @param account deleted {@link Account}
	 */
	public void accountDeleted(final Account account) {
		if (account == null)
			throw new IllegalArgumentException("account is null");

		send(new AccountDeleteEventData(account));
	}
}
