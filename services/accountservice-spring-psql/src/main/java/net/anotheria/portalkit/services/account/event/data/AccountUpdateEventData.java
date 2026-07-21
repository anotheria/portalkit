package net.anotheria.portalkit.services.account.event.data;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.event.AccountServiceOperationType;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;

/**
 * {@link ServiceEventData} for account update operation.
 *
 * @author Alex Osadchy
 */
public final class AccountUpdateEventData extends ServiceEventData {

	/**
	 * Generated version number.
	 */
	private static final long serialVersionUID = 2229140460422771618L;

	/**
	 * {@link Account} before update.
	 */
	private Account beforeUpdate;

	/**
	 * {@link Account} after update.
	 */
	private Account afterUpdate;

	/**
	 * Constructor.
	 *
	 * @param beforeUpdate {@link Account} before update
	 * @param afterUpdate {@link Account} after update
	 */
	public AccountUpdateEventData(Account beforeUpdate, Account afterUpdate) {
		this.beforeUpdate = beforeUpdate;
		this.afterUpdate = afterUpdate;
	}

	@Override
	public String getOperationType() {
		return AccountServiceOperationType.ACCOUNT_UPDATE.name();
	}

	public Account getBeforeUpdate() {
		return beforeUpdate;
	}

	public Account getAfterUpdate() {
		return afterUpdate;
	}
}
