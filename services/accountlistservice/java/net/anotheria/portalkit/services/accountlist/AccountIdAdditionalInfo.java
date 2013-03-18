package net.anotheria.portalkit.services.accountlist;

import java.io.Serializable;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * 
 * @author dagafonov
 * 
 */
public class AccountIdAdditionalInfo implements Serializable {

	private static final long serialVersionUID = 6018520772456445957L;

	private AccountId accountId;
	private String additionalInfo;
	private long creationTimestamp;

	public AccountIdAdditionalInfo(AccountId accountId, String additionalInfo) {
		this.accountId = accountId;
		this.additionalInfo = additionalInfo;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
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
		AccountIdAdditionalInfo other = (AccountIdAdditionalInfo) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountIdAdditionalInfo [accountId=" + accountId + ", additionalInfo=" + additionalInfo + "]";
	}

}
