package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.persistence.subscription.TransactionDO;

/**
 * @author Vlad Lukjanenko
 */
public class Transaction {

    /**
     * Id of transaction.
     * */
    private String transactionId;
    /**
     * Id of subscription.
     * */
    private String subscriptionId;
    /**
     * AccountId of the subscription owner.
     */
    private AccountId accountId;
    /**
     * Id of the purchase product.
     */
    private String productId;
    /**
     * Timestamp of transaction.
     * */
    private long timestamp;
    /**
     * Number of time prolongation was performed.
     * */
    private int prolongationCount;
    /**
     * Amount.
     * */
    private int amount;


    public Transaction() {}

    public Transaction(Subscription subscription) {

        this.subscriptionId = subscription.getSubscriptionId();
        this.accountId = subscription.getAccountId();
        this.productId = subscription.getProductId();
        this.timestamp = subscription.getPurchaseTimestamp();
        this.prolongationCount = subscription.getProlongationCount();
        this.amount = subscription.getAmountInCents();
    }

    public Transaction(TransactionDO transactionDO) {

        this.transactionId = transactionDO.getTransactionId();
        this.subscriptionId = transactionDO.getSubscriptionId();
        this.accountId =  new AccountId(transactionDO.getAccountId());
        this.productId = transactionDO.getProductId();
        this.timestamp = transactionDO.getTimestamp();
        this.prolongationCount = transactionDO.getProlongationCount();
        this.amount = transactionDO.getAmount();
    }


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

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
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

    public TransactionDO toDO() {

        TransactionDO transactionDO = new TransactionDO();

        transactionDO.setTransactionId(this.transactionId);
        transactionDO.setSubscriptionId(this.subscriptionId);
        transactionDO.setAccountId(this.accountId.getInternalId());
        transactionDO.setProductId(this.productId);
        transactionDO.setTimestamp(this.timestamp);
        transactionDO.setProlongationCount(this.prolongationCount);
        transactionDO.setAmount(this.amount);

        return transactionDO;
    }
}
