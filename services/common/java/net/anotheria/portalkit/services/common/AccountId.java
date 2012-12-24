package net.anotheria.portalkit.services.common;

import java.io.Serializable;

import net.anotheria.portalkit.services.common.id.IdGenerator;
import net.anotheria.util.StringUtils;

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
	 * 
	 * @param aInternalId
	 *            internal id
	 */
	public AccountId(final String aInternalId) {
		if (StringUtils.isEmpty(aInternalId))
			throw new IllegalArgumentException("aInternalId is empty.");

		internalId = aInternalId;
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
		return new AccountId(internalId);
	}

}
