package net.anotheria.portalkit.services.account;

/**
 * This class contains constants for account statuses that can be shared between different projects.
 *
 * @author lrosenberg
 * @since 18.01.13 10:15
 */
public class AccountStatusConstants {
	/**
	 * Everything is ok.
	 */
	public static final long STATUS_OK = 1;
	/**
	 * User is blocked, whatever that means in the context of an application.
	 */
	public static final long STATUS_BLOCKED = 2;
	/**
	 * Email is confirmed (reg confirmation link clicked).
	 */
	public static final long STATUS_EMAIL_CONFIRMED = 4;
	/**
	 * Email bounces, the email address is considered valid, but there are bounces.
	 */
	public static final long STATUS_EMAIL_BOUNCE = 8;


}
