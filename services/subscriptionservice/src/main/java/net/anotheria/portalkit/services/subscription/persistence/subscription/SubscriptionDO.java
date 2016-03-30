package net.anotheria.portalkit.services.subscription.persistence.subscription;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Vlad Lukjanenko
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sub_subscription")
@NamedQueries({
        @NamedQuery(
                name = SubscriptionDO.JPQL_GET_ACTIVE_BY_ACCOUNT_ID,
                query = "select s from SubscriptionDO s where s.accountId = :accountId and s.active = :active"
        ),
        @NamedQuery(
                name = SubscriptionDO.JPQL_GET_BY_ACCOUNT_ID,
                query = "select s from SubscriptionDO s where s.accountId = :accountId"
        ),
        @NamedQuery(
                name = SubscriptionDO.JPQL_GET_ALL,
                query = "select s from SubscriptionDO s"
        )
})
public class SubscriptionDO {

    public static final String JPQL_GET_ACTIVE_BY_ACCOUNT_ID = "SubscriptionDO.getActiveByAccountId";
    public static final String JPQL_GET_BY_ACCOUNT_ID = "SubscriptionDO.getByAccountId";
    public static final String JPQL_GET_ALL = "SubscriptionDO.getSubscriptions";

    /**
     * AccountId of the subscription owner.
     */
    @Column
    private String accountId;
    /**
     * Subscription id.
     */
    @Column @Id
    private String subscriptionId;
    /**
     * Id of the purchase product.
     */
    @Column
    private String productId;
    /**
     * If true the subscription is active. Only active subscription can be prolonged. One account should have only one active subscription.
     */
    @Column
    private boolean active;
    /**
     * Current expiration timestamp. If the current date passes the expiration timestamp and the subscription is still active, the subscription should be prolonged.
     */
    @Column
    private long expirationTimestamp;
    /**
     * Number of prolongations this subscription already passed.
     */
    @Column
    private int prolongationCount;

    /**
     * Timestamp when the subscription has been purchased initially.
     */
    @Column
    private long purchaseTimestamp;
    /**
     * Last prolongation timestamp of the subscription. Prolongation is usually an automatical process.
     */
    @Column
    private long lastProlongationTimestamp;

    /**
     * Period of time the cancelation must be issued before the next prolongation.
     */
    @Column
    private long cancelationPeriodInMillis;

    /**
     * Timestamp the current subscription has been canceled.
     */
    @Column
    private long cancelationTimestamp;

    /**
     * This flag identifies that the subscription has been canceled, but not within current period. It indicated for the future that the cancelation has to be issued after next prolongation.
     */
    @Column
    private boolean preparedForCancelation;

    /**
     * Currency the user is paying.
     */
    @Column
    private String currency;

    /**
     * Amount the user is paying.
     */
    @Column
    private int amountInCents;

    /**
     * Duration of the subscription as string. This field is string to allow to use different duration codes, for example 3M, 2W, 30D etc.
     */
    @Column
    private String duration;


    public SubscriptionDO() {

    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public int getProlongationCount() {
        return prolongationCount;
    }

    public void setProlongationCount(int prolongationCount) {
        this.prolongationCount = prolongationCount;
    }

    public long getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    public void setPurchaseTimestamp(long purchaseTimestamp) {
        this.purchaseTimestamp = purchaseTimestamp;
    }

    public long getLastProlongationTimestamp() {
        return lastProlongationTimestamp;
    }

    public void setLastProlongationTimestamp(long lastProlongationTimestamp) {
        this.lastProlongationTimestamp = lastProlongationTimestamp;
    }

    public long getCancelationPeriodInMillis() {
        return cancelationPeriodInMillis;
    }

    public void setCancelationPeriodInMillis(long cancelationPeriodInMillis) {
        this.cancelationPeriodInMillis = cancelationPeriodInMillis;
    }

    public long getCancelationTimestamp() {
        return cancelationTimestamp;
    }

    public void setCancelationTimestamp(long cancelationTimestamp) {
        this.cancelationTimestamp = cancelationTimestamp;
    }

    public boolean isPreparedForCancelation() {
        return preparedForCancelation;
    }

    public void setPreparedForCancelation(boolean preparedForCancelation) {
        this.preparedForCancelation = preparedForCancelation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(int amountInCents) {
        this.amountInCents = amountInCents;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subscriptionId, this.accountId);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof SubscriptionDO)) {
            return false;
        }

        SubscriptionDO subscription = (SubscriptionDO) obj;

        if (!this.accountId.equals(subscription.getAccountId())) {
            return false;
        }

        if (!this.subscriptionId.equals(subscription.getSubscriptionId())) {
            return false;
        }

        if (!this.productId.equals(subscription.getProductId())) {
            return false;
        }

        return true;
    }
}
