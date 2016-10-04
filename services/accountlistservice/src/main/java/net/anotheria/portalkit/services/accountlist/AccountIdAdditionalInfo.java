package net.anotheria.portalkit.services.accountlist;

import java.io.Serializable;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Container for addition information like creation time stamp and anything that
 * can be transformed into string value.
 * 
 * @author dagafonov
 * 
 */
public class AccountIdAdditionalInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5791442539361096897L;

	/**
	 * 
	 */
	private AccountId accountId;

	/**
	 * 
	 */
	private String additionalInfo;

	/**
	 * 
	 */
	private long creationTimestamp;

	/**
	 * 
	 * @param accountId account id.
	 */
	public AccountIdAdditionalInfo(AccountId accountId) {
		this.accountId = accountId;
		setAdditionalInfo("");
	}

	/**
	 * 
	 * @param accountId account id.
	 * @param additionalInfo additional info.
	 */
	public AccountIdAdditionalInfo(AccountId accountId, String additionalInfo) {
		this(accountId);
		setAdditionalInfo(additionalInfo);
	}

	/**
	 * 
	 * @param accountId account id.
	 * @param additionalInfo additional info.
	 * @param creationTimestamp	timestamp.
	 */
	public AccountIdAdditionalInfo(AccountId accountId, String additionalInfo, long creationTimestamp) {
		this(accountId, additionalInfo);
		this.creationTimestamp = creationTimestamp;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	/**
	 * 
	 * @param additionalInfo additional info.	additional info.
	 */
	public void setAdditionalInfo(String additionalInfo) {
		if (additionalInfo != null) {
			this.additionalInfo = additionalInfo;
		} else {
			this.additionalInfo = "";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		return result;
	}

	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountIdAdditionalInfo other = AccountIdAdditionalInfo.class.cast(obj);
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountIdAdditionalInfo [accountId=" + accountId + ", additionalInfo=" + additionalInfo + ", creationTimestamp=" + creationTimestamp
				+ "]";
	}

}
