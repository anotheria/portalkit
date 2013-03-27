package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;

/**
 * ForeignId bean.
 * 
 * @author lrosenberg
 * @since 28.12.12 23:31
 */
public class ForeignId implements Serializable {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -3724518051843205604L;

	/**
	 * The source id, this is dependent on the concrete source.
	 */
	private String id;

	/**
	 * The id of the source. This is application specific. However, we have
	 * collected some common sourceids in ForeignIdSources class.
	 */
	private int sourceId;

	/**
	 * The account id in our system.
	 */
	private AccountId accountId;

	/**
	 * Default constructor.
	 */
	public ForeignId() {

	}

	/**
	 * Additional constructor that fills needed fields.
	 * 
	 * @param accountId
	 * @param sourceId
	 * @param id
	 */
	public ForeignId(AccountId accountId, int sourceId, String id) {
		this.accountId = accountId;
		this.sourceId = sourceId;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + sourceId;
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
		ForeignId other = (ForeignId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sourceId != other.sourceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ForeignId [id=" + id + ", sourceId=" + sourceId + ", accountId=" + accountId + "]";
	}

}
