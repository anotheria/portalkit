package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Builder for transaction log entry objects.
 *
 * @author lrosenberg
 * @since 13.03.16 16:18
 */
public class TransactionLogEntryBuilder {
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

	public TransactionLogEntryBuilder accountId(AccountId accountId) {
		this.accountId = accountId; return this;
	}

	public TransactionLogEntryBuilder accountId(String accountId) {
		this.accountId = new AccountId(accountId); return this;
	}

	public TransactionLogEntryBuilder action(String action) {
		this.action = action; return this;
	}

	public TransactionLogEntryBuilder message(String message) {
		this.message = message; return this;
	}

	public TransactionLogEntryBuilder productId(String productId) {
		this.productId = productId; return this;
	}

	public TransactionLogEntryBuilder subscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId; return this;
	}

	public TransactionLogEntryBuilder timestamp(long timestamp) {
		this.timestamp = timestamp; return this;
	}

	public TransactionLogEntryBuilder transactionId(String transactionId) {
		this.transactionId = transactionId; return this;
	}

	public TransactionLogEntry build(){
		TransactionLogEntry ret = new TransactionLogEntry();

		ret.setAccountId(accountId);
		ret.setAction(action);
		ret.setProductId(productId);
		ret.setTimestamp(timestamp);
		ret.setMessage(message);
		ret.setSubscriptionId(subscriptionId);
		ret.setTransactionId(transactionId);
		return ret;
	}
}
