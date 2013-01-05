package net.anotheria.portalkit.services.account;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.12.12 16:04
 */
public class AccountServiceConfig {
	private boolean exclusiveMail = true;
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
