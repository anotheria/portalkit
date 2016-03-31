package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;

/**
 * A single transaction entry should represent a state change of the subscription or user status. The exception is the DEBUG action type, which allows to store
 * general information for user.
 *
 * @author lrosenberg
 * @since 12.03.16 17:03
 */
public class TransactionLogEntry implements Serializable{
	/**
	 * Id of the transaction.
	 */
	private String transactionId;
	/**
	 * Id of the associated subscription (if applicable).
	 */
	private long subscriptionId;
	/**
	 * AccountId.
	 */
	private AccountId accountId;
	/**
	 * Product Id of the corresponding product.
	 */
	private String productId;
	/**
	 * When did then transaction occured.
	 */
	private long timestamp = System.currentTimeMillis();
	/**
	 * Transaction action. This is implementation specific, but should be a limited set of constraints. For example:
	 * INIT, DEBUG, DEBIT, SCHEDULE, DESCHEDULE, CANCEL.
	 */
	private String action;
	/**
	 * Free text message.
	 */
	private String message;

	public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String toString() {
		return "TransactionLogEntry{" +
				"accountId=" + accountId +
				", transactionId='" + transactionId + '\'' +
				", subscriptionId='" + subscriptionId + '\'' +
				", productId='" + productId + '\'' +
				", timestamp=" + timestamp +
				", action='" + action + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
