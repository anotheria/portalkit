package net.anotheria.portalkit.services.account.event.data;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.event.AccountServiceOperationType;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;

/**
 * {@link ServiceEventData} for account delete operation.
 *
 * @author Alex Osadchy
 */
public final class AccountDeleteEventData extends ServiceEventData {

	/**
	 * Generated version number.
	 */
	private static final long serialVersionUID = -2845129042665182049L;

	/**
	 * Deleted {@link Account}.
	 */
	private Account account;

	/**
	 * Constructor.
	 *
	 * @param account deleted {@link Account}
	 */
	public AccountDeleteEventData(Account account) {
		this.account = account;
	}

	@Override
	public String getOperationType() {
		return AccountServiceOperationType.ACCOUNT_DELETE.name();
	}

	public Account getAccount() {
		return account;
	}
}
