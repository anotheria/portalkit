package net.anotheria.portalkit.services.common;

import net.anotheria.portalkit.services.common.id.IdGenerator;
import net.anotheria.util.StringUtils;

import java.io.Serializable;

/**
 * Account unique identifier.
 * 
 * @author Leon Rosenberg, Alexandr Bolbat
 */
public class AccountId implements Serializable, Cloneable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 2996966698795723273L;

	/**
	 * Internal id.
	 */
	private String internalId;

	/**
	 * Default constructor.
	 */
	public AccountId() {
	}

	/**
	 * Default constructor.
	 * 
	 * @param aInternalId
	 *            internal id
	 */
	public AccountId(final String aInternalId) {
		if (StringUtils.isEmpty(aInternalId))
			throw new IllegalArgumentException("aInternalId is empty.");

		this.internalId = aInternalId;
	}

	public void setInternalId(final String aInternalId) {
		if (StringUtils.isEmpty(aInternalId))
			throw new IllegalArgumentException("aInternalId is empty.");

		this.internalId = aInternalId;
	}

	public String getInternalId() {
		return internalId;
	}

	/**
	 * Generate new unique {@link AccountId}.
	 * 
	 * @return generated {@link AccountId}
	 */
	public static final AccountId generateNew() {
		return new AccountId(IdGenerator.generateUniqueRandomId());
	}

	@Override
	public String toString() {
		return internalId;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof AccountId && internalId.equals(((AccountId) o).internalId);
	}

	@Override
	public int hashCode() {
		return internalId.hashCode();
	}

	@Override
	public AccountId clone() {
		try {
			return (AccountId) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Clone not supported but it should be in AccountId");
		}
	}

	/**
	 * Returns a lexigraphically sorted pair of account ids. This is useful if you need a lock on two objects (user a writes to user b) and you want to do it
	 * always in the same order to prevent deadlocks.
	 * @param first
	 * @param second
	 * @return
	 */
	public static AccountIdPair getSortedPair(AccountId first, AccountId second){
		return (first.internalId.compareTo(second.internalId)<0) ?
				new AccountIdPair(first, second) :
				new AccountIdPair(second, first);

	}


}
