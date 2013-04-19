package net.anotheria.portalkit.services.account.events;

import java.io.Serializable;

import net.anotheria.portalkit.services.account.Account;

/**
 * Event class for account service.
 * 
 * @author dagafonov
 * 
 */
public class AccountEvent implements Serializable {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -5160796863875442598L;

	/**
	 * Event type of account operation.
	 */
	public static enum AccountEventOperation {

		/**
		 * 
		 */
		CREATE,

		/**
		 * 
		 */
		UPDATE,

		/**
		 * 
		 */
		DELETE;
	}

	/**
	 * Account.
	 */
	private Account account;

	/**
	 * Operation type.
	 */
	private AccountEventOperation eventType;

	/**
	 * Timestamp of event.
	 */
	private long eventCreationTime = System.currentTimeMillis();

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public AccountEventOperation getEventType() {
		return eventType;
	}

	public void setEventType(AccountEventOperation eventType) {
		this.eventType = eventType;
	}

	public long getEventCreationTime() {
		return eventCreationTime;
	}

	/**
	 * 
	 * @param account
	 * @return {@link AccountEvent}
	 */
	public static AccountEvent create(Account account) {
		AccountEvent event = new AccountEvent();
		event.setAccount(account);
		event.setEventType(AccountEventOperation.CREATE);
		return event;
	}

	/**
	 * 
	 * @param account
	 * @return {@link AccountEvent}
	 */
	public static AccountEvent delete(Account account) {
		AccountEvent event = new AccountEvent();
		event.setAccount(account);
		event.setEventType(AccountEventOperation.DELETE);
		return event;
	}

	@Override
	public String toString() {
		return "AccountEvent [account=" + account + ", eventType=" + eventType + ", eventCreationTime=" + eventCreationTime + "]";
	}

}
