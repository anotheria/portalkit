package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.Objects;

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
	 * The foreign id, this is dependent on the concrete source.
	 */
	private String foreignId;

	/**
	 * The id of the source.
	 */
	private String sourceId;

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
	 * @param accountId	account id.
	 * @param foreignId foreign id.
	 * @param sourceId source id.
	 */
	public ForeignId(AccountId accountId, String foreignId, String sourceId) {
		this.accountId = accountId;
		this.foreignId = foreignId;
		this.sourceId = sourceId;
	}

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public AccountId getAccountId() {
		return accountId;
	}

	public void setAccountId(AccountId accountId) {
		this.accountId = accountId;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ForeignId foreignId1 = (ForeignId) o;
        return Objects.equals(foreignId, foreignId1.foreignId) && Objects.equals(sourceId, foreignId1.sourceId) && Objects.equals(accountId, foreignId1.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignId, sourceId, accountId);
    }

    @Override
    public String toString() {
        return "ForeignId{" +
                "foreignId='" + foreignId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", accountId=" + accountId +
                '}';
    }
}
