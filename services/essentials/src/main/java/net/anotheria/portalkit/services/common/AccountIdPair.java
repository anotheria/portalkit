package net.anotheria.portalkit.services.common;

/**
 * Represents a pair of accountids. This is useful if you need to perform locking operation on two accounts on want to
 * do it always in the same order to prevent deadlocks.
 *
 * @author lrosenberg
 * @since 2019-08-20 14:38
 */
public class AccountIdPair {
	/**
	 * AccountId with lower compare value.
	 */
	private AccountId lower;
	/**
	 * AccountId with higher compare value.
	 */
	private AccountId higher;

	public AccountIdPair(AccountId lower, AccountId higher) {
		this.lower = lower;
		this.higher = higher;
	}

	public AccountId getLower() {
		return lower;
	}

	public void setLower(AccountId lower) {
		this.lower = lower;
	}

	public AccountId getHigher() {
		return higher;
	}

	public void setHigher(AccountId higher) {
		this.higher = higher;
	}
}
