package net.anotheria.portalkit.services.account.event.data;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.event.AccountServiceOperationType;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;

/**
 * {@link ServiceEventData} for create account operation.
 *
 * @author Alex Osadchy
 */
public final class AccountCreateEventData extends ServiceEventData {

	/**
	 * Generated version number.
	 */
	private static final long serialVersionUID = -5318721488710931619L;

	/**
	 * Created {@link Account}.
	 */
	private Account account;

	/**
	 * Constructor.
	 *
	 * @param account {@link Account}
	 */
	public AccountCreateEventData(Account account) {
		this.account = account;
	}

	@Override
	public String getOperationType() {
		return AccountServiceOperationType.ACCOUNT_CREATE.name();
	}

	public Account getAccount() {
		return account;
	}
}
