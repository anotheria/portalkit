package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.persistence.subscription.SubscriptionDO;

import java.io.Serializable;
import java.util.Objects;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.03.16 16:05
 */
public class Subscription implements Serializable {

	/**
	 * Generated serial version UID.
	 * */
	private static final long serialVersionUID = 3076240992969499175L;

	/**
	 * AccountId of the subscription owner.
	 */
	private AccountId accountId;
	/**
	 * Subscription id.
	 */
	private long subscriptionId;
	/**
	 * Id of the purchase product.
	 */
	private String productId;
	/**
	 * If true the subscription is active. Only active subscription can be prolonged. One account should have only one active subscription.
	 */
	private boolean active;
	/**
	 * Is user has VIP status.
	 */
	private boolean isVip;
	/**
	 * Current expiration timestamp. If the current date passes the expiration timestamp and the subscription is still active, the subscription should be prolonged.
	 */
	private long expirationTimestamp;
	/**
	 * Number of prolongations this subscription already passed.
	 */
	private int prolongationCount;

	/**
	 * Timestamp when the subscription has been purchased initially.
	 */
	private long purchaseTimestamp;
	/**
	 * Last prolongation timestamp of the subscription. Prolongation is usually an automatical process.
	 */
	private long lastProlongationTimestamp;

	/**
	 * Period of time the cancelation must be issued before the next prolongation.
	 */
	private long cancelationPeriodInMillis;

	/**
	 * Timestamp the current subscription has been canceled.
	 */
	private long cancelationTimestamp;

	/**
	 * This flag identifies that the subscription has been canceled, but not within current period. It indicated for the future that the cancelation has to be issued after next prolongation.
	 */
	private boolean preparedForCancelation;

	/**
	 * Currency the user is paying.
	 */
	private String currency;

	/**
	 * Amount the user is paying.
	 */
	private int amountInCents;

	/**
	 * Duration of the subscription as string. This field is string to allow to use different duration codes, for example 3M, 2W, 30D etc.
	 */
	private String duration;

	/**
	 * Payment device type.
	 */
	private String paymentDevice;


	public Subscription() {}

	public Subscription(SubscriptionDO subscriptionDO) {

		this.accountId = new AccountId(subscriptionDO.getAccountId());
		this.active = subscriptionDO.isActive();
		this.isVip = subscriptionDO.isVip();
		this.expirationTimestamp = subscriptionDO.getExpirationTimestamp();
		this.amountInCents = subscriptionDO.getAmountInCents();
		this.cancelationPeriodInMillis = subscriptionDO.getCancelationPeriodInMillis();
		this.cancelationTimestamp = subscriptionDO.getCancelationTimestamp();
		this.currency = subscriptionDO.getCurrency();
		this.duration = subscriptionDO.getDuration();
		this.productId = subscriptionDO.getProductId();
		this.preparedForCancelation = subscriptionDO.isPreparedForCancelation();
		this.lastProlongationTimestamp = subscriptionDO.getLastProlongationTimestamp();
		this.prolongationCount = subscriptionDO.getProlongationCount();
		this.purchaseTimestamp = subscriptionDO.getPurchaseTimestamp();
		this.subscriptionId = subscriptionDO.getSubscriptionId();
		this.paymentDevice = subscriptionDO.getPaymentDevice();
	}


	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean vip) {
		isVip = vip;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
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

	public String getPaymentDevice() {
		return paymentDevice;
	}

	public void setPaymentDevice(String paymentDevice) {
		this.paymentDevice = paymentDevice;
	}

	public SubscriptionDO toDO() {

		SubscriptionDO subscriptionDO = new SubscriptionDO();

		subscriptionDO.setAccountId(this.accountId.getInternalId());
		subscriptionDO.setActive(this.active);
		subscriptionDO.setVip(this.isVip);
		subscriptionDO.setAmountInCents(this.amountInCents);
		subscriptionDO.setCancelationPeriodInMillis(this.cancelationPeriodInMillis);
		subscriptionDO.setCancelationTimestamp(this.cancelationTimestamp);
		subscriptionDO.setCurrency(this.currency);
		subscriptionDO.setDuration(this.duration);
		subscriptionDO.setExpirationTimestamp(this.expirationTimestamp);
		subscriptionDO.setLastProlongationTimestamp(this.lastProlongationTimestamp);
		subscriptionDO.setPreparedForCancelation(this.preparedForCancelation);
		subscriptionDO.setProductId(this.productId);
		subscriptionDO.setProlongationCount(this.prolongationCount);
		subscriptionDO.setPurchaseTimestamp(this.purchaseTimestamp);
		subscriptionDO.setSubscriptionId(this.subscriptionId);
		subscriptionDO.setPaymentDevice(this.paymentDevice);

		return subscriptionDO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.subscriptionId, this.accountId);
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Subscription)) {
			return false;
		}

		Subscription subscription = (Subscription) obj;

		if (!this.accountId.equals(subscription.getAccountId())) {
			return false;
		}

		if (this.subscriptionId != subscription.getSubscriptionId()) {
			return false;
		}

		if (!this.productId.equals(subscription.getProductId())) {
			return false;
		}

		return true;
	}
}
















