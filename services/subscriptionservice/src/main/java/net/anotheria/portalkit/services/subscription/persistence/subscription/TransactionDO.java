package net.anotheria.portalkit.services.subscription.persistence.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.Transaction;

import javax.persistence.*;

/**
 * @author Vlad Lukjanenko
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sub_transaction")
@NamedQueries({
        @NamedQuery(
                name = TransactionDO.JPQL_GET_BY_ACCOUNT_ID,
                query = "select t from TransactionDO t where t.accountId = :accountId"
        ),
        @NamedQuery(
                name = TransactionDO.JPQL_GET_ALL,
                query = "select t from TransactionDO t"
        )
})
public class TransactionDO {

    public static final String JPQL_GET_BY_ACCOUNT_ID = "TransactionDO.getByAccountId";
    public static final String JPQL_GET_ALL = "TransactionDO.getTransactions";

    /**
     * Id of transaction.
     * */
    @Column @Id
    private String transactionId;
    /**
     * Id of subscription.
     * */
    @Column
    private String subscriptionId;
    /**
     * AccountId of the subscription owner.
     */
    @Column
    private String accountId;
    /**
     * Id of the purchase product.
     */
    @Column
    private String productId;
    /**
     * Timestamp of transaction.
     * */
    @Column(name = "transaction_timestamp")
    private long timestamp;
    /**
     * Number of time prolongation was performed.
     * */
    @Column
    private int prolongationCount;
    /**
     * Amount.
     * */
    @Column
    private int amount;


    public TransactionDO() {}


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getProlongationCount() {
        return prolongationCount;
    }

    public void setProlongationCount(int prolongationCount) {
        this.prolongationCount = prolongationCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
