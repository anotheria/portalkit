package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.crypt.MD5Util;

import java.io.Serializable;

/**
 * 
 * @author dagafonov
 *
 */
public class AccountSettingsKey implements Serializable {

	/**
	 * Generated default serialVersionUID.
	 */
	private static final long serialVersionUID = 5884631638594339728L;

	/**
	 * Account ID.
	 */
	private String accountId;
	
	/**
	 * Dataspace ID.
	 */
	private int dataspaceId;

	public AccountSettingsKey(){

	}

	public AccountSettingsKey(AccountId anAccountId, int aDataspaceId){
		accountId = anAccountId.getInternalId();
		dataspaceId = aDataspaceId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getDataspaceId() {
		return dataspaceId;
	}

	public void setDataspaceId(int dataspaceId) {
		this.dataspaceId = dataspaceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + dataspaceId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountSettingsKey other = (AccountSettingsKey) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (dataspaceId != other.dataspaceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(accountId);
		b.append("-");
		b.append(dataspaceId);
		return MD5Util.getMD5Hash(b);
	}

}
