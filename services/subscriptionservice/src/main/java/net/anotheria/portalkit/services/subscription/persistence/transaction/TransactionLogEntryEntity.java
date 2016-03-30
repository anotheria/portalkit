package net.anotheria.portalkit.services.subscription.persistence.transaction;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.TransactionLogEntry;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 16:34
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sub_transaction_log")
@NamedQuery(
		name = "TransactionLogEntryEntity_get_by_account_id",
		query ="SELECT t from TransactionLogEntryEntity t where t.accountId = :accountId" )
public class TransactionLogEntryEntity {

	@Column @Id  @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long technicalId;

	/**
	 * Id of the transaction.
	 */
	@Column
	private long transactionId;
	/**
	 * Id of the associated subscription (if applicable).
	 */
	@Column
	private String subscriptionId;
	/**
	 * AccountId.
	 */
	@Column
	private String accountId;
	/**
	 * Product Id of the corresponding product.
	 */
	@Column
	private String productId;
	/**
	 * When did then transaction occured.
	 */
	@Column
	private long timestamp = System.currentTimeMillis();
	/**
	 * Transaction action. This is implementation specific, but should be a limited set of constraints. For example:
	 * INIT, DEBUG, DEBIT, SCHEDULE, DESCHEDULE, CANCEL.
	 */
	@Column
	private String action;
	/**
	 * Free text message.
	 */
	@Column
	private String message;

	@Column
	private Timestamp dao_created;

	@Column
	private Timestamp dao_updated;

	public TransactionLogEntryEntity(){

	}

	public TransactionLogEntryEntity(TransactionLogEntry entry){
		this.transactionId = entry.getTransactionId();
		this.subscriptionId = entry.getSubscriptionId();
		this.accountId = entry.getAccountId().getInternalId();
		this.productId = entry.getProductId();
		this.timestamp = entry.getTimestamp();
		this.action = entry.getAction();
		this.message = entry.getMessage();

		this.dao_created = new Timestamp(System.currentTimeMillis());

	}

	public TransactionLogEntry toBusinessObject(){
		TransactionLogEntry entry = new TransactionLogEntry();
		entry.setTransactionId(this.transactionId);
		entry.setSubscriptionId(this.subscriptionId);
		entry.setAccountId(new AccountId(this.accountId));
		entry.setProductId(this.productId);
		entry.setTimestamp(this.timestamp);
		entry.setAction(this.action);
		entry.setMessage(this.message);
		return entry;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getDao_created() {
		return dao_created;
	}

	public void setDao_created(Timestamp dao_created) {
		this.dao_created = dao_created;
	}

	public Timestamp getDao_updated() {
		return dao_updated;
	}

	public void setDao_updated(Timestamp dao_updated) {
		this.dao_updated = dao_updated;
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

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
}
