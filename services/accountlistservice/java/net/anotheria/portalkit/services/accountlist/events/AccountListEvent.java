package net.anotheria.portalkit.services.accountlist.events;

import java.io.Serializable;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Account list event object class.
 * 
 * @author dagafonov
 * 
 */
public class AccountListEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8574571454881155640L;

	/**
	 * Who created event.
	 */
	private AccountId ownerId;

	/**
	 * With who linked account list record.
	 */
	private AccountId target;

	/**
	 * List name.
	 */
	private String listName;

	/**
	 * Event type.
	 */
	private AccountListEventType eventType;

	/**
	 * Event creation time.
	 */
	private long eventCreationTime;

	/**
	 * Describes operation types.
	 */
	public static enum AccountListEventType {
		
		/**
		 * Create event operation.
		 */
		CREATE, 
		
		/**
		 * Update event operation.
		 */
		UPDATE, 
		 
		 /**
		  * Delete event operation.
		  */
		 DELETE;
	}

	public AccountId getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(AccountId ownerId) {
		this.ownerId = ownerId;
	}

	public AccountId getTarget() {
		return target;
	}

	public void setTarget(AccountId target) {
		this.target = target;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public AccountListEventType getEventType() {
		return eventType;
	}

	public void setEventType(AccountListEventType eventType) {
		this.eventType = eventType;
	}

	public long getEventCreationTime() {
		return eventCreationTime;
	}

	public void setEventCreationTime(long eventCreationTime) {
		this.eventCreationTime = eventCreationTime;
	}

	@Override
	public String toString() {
		return "AccountListEvent [ownerId=" + ownerId + ", target=" + target + ", listName=" + listName + ", eventType=" + eventType
				+ ", eventCreationTime=" + eventCreationTime + "]";
	}

	private static AccountListEvent instance(AccountId owner, AccountId target, String listName, AccountListEventType eventType) {
		AccountListEvent result = new AccountListEvent();
		result.setOwnerId(owner);
		result.setTarget(target);
		result.setListName(listName);
		result.setEventCreationTime(System.currentTimeMillis());
		result.setEventType(eventType);
		return result;
	}

	/**
	 * Creates instance for create event operation.
	 * @param owner
	 * @param target
	 * @param listName
	 * @return {@link AccountListEvent}
	 */
	public static AccountListEvent create(AccountId owner, AccountId target, String listName) {
		return instance(owner, target, listName, AccountListEventType.CREATE);
	}

	/**
	 * Updates instance for create event operation.
	 * @param owner
	 * @param target
	 * @param listName
	 * @return {@link AccountListEvent}
	 */
	public static AccountListEvent update(AccountId owner, AccountId target, String listName) {
		return instance(owner, target, listName, AccountListEventType.UPDATE);
	}

	/**
	 * Deletes instance for create event operation.
	 * @param owner
	 * @param target
	 * @param listName
	 * @return {@link AccountListEvent}
	 */
	public static AccountListEvent delete(AccountId owner, AccountId target, String listName) {
		return instance(owner, target, listName, AccountListEventType.DELETE);
	}

}
