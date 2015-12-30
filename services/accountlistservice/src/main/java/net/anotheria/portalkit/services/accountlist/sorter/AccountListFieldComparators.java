package net.anotheria.portalkit.services.accountlist.sorter;

import java.util.Comparator;

import net.anotheria.portalkit.services.accountlist.AccountIdAdditionalInfo;

/**
 * {@link Enum} that represents list of all fields involved into sorting process of
 * {@link AccountIdAdditionalInfo}.
 * 
 * @author dagafonov
 * 
 */
public enum AccountListFieldComparators implements FieldComparator<AccountIdAdditionalInfo> {

	/**
	 * CREATION_TIMESTAMP field.
	 */
	CREATION_TIMESTAMP("creationTimestamp") {
		@Override
		public Comparator<AccountIdAdditionalInfo> getComparator() {
			return new Comparator<AccountIdAdditionalInfo>() {
				@Override
				public int compare(AccountIdAdditionalInfo left, AccountIdAdditionalInfo right) {
					return Long.valueOf(left.getCreationTimestamp()).compareTo(Long.valueOf(right.getCreationTimestamp()));
				}
			};
		}
	};

	/**
	 * Field name.
	 */
	private String fieldName;

	private AccountListFieldComparators(final String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Gets {@link AccountListFieldComparators} by name.
	 * 
	 * @param orderBy
	 * @return {@link AccountListFieldComparators}
	 */
	public static AccountListFieldComparators find(final String orderBy) {
		for (AccountListFieldComparators is : values()) {
			if (is.name().equals(orderBy)) {
				return is;
			}
		}
		return AccountListFieldComparators.CREATION_TIMESTAMP;
	}

	public String getName() {
		return name();
	}

	/**
	 * Gets field comparator.
	 * 
	 * @return {@link Comparator<AccountIdAdditionalInfo>}
	 */
	@Override
	public abstract Comparator<AccountIdAdditionalInfo> getComparator();
}