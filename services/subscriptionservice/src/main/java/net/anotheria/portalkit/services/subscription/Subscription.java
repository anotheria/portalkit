package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.03.16 16:05
 */
public class Subscription {
	/**
	 * AccountId of the subscription owner.
	 */
	private AccountId accountId;
	/**
	 * Subscription id.
	 */
	private String subscriptionId;
	/**
	 * Id of the purchase product.
	 */
	private String productId;
	/**
	 * If true the subscription is active. Only active subscription can be prolonged. One account should have only one active subscription.
	 */
	private boolean active;
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
	private boolean cancelationTimestamp;

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
}
