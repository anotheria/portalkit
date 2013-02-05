package net.anotheria.portalkit.services.account;

/**
 * Configuration for account service.
 *
 * @author lrosenberg
 * @since 13.12.12 16:04
 */
public class AccountServiceConfig {
	/**
	 * If true only one account with same email address is allowed. Default true.
	 */
	private boolean exclusiveMail = true;
	/**
	 * If true only one account with same name is allowed. Default true.
	 */
	private boolean exclusiveName = true;

	public boolean isExclusiveMail() {
		return exclusiveMail;
	}

	public void setExclusiveMail(boolean exclusiveMail) {
		this.exclusiveMail = exclusiveMail;
	}

	public boolean isExclusiveName() {
		return exclusiveName;
	}

	public void setExclusiveName(boolean exclusiveName) {
		this.exclusiveName = exclusiveName;
	}
}
