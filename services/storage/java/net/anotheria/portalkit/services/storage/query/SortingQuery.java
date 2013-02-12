package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.value.QuerySortingValue;

/**
 * {@link SortingQuery}, works only in root scope.
 * 
 * @author Alexandr Bolbat
 */
public final class SortingQuery implements Query {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -5902870887923547566L;

	/**
	 * Field name.
	 */
	private final String fieldName;

	/**
	 * Sorting value.
	 */
	private final QuerySortingValue sortingValue;

	/**
	 * Default constructor.
	 * 
	 * @param aFieldName
	 *            field name
	 * @param aSortingValue
	 *            sorting value
	 */
	private SortingQuery(final String aFieldName, final QuerySortingValue aSortingValue) {
		this.fieldName = aFieldName;
		this.sortingValue = aSortingValue;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public QuerySortingValue getQueryValue() {
		return sortingValue;
	}

	/**
	 * Create new instance of {@link SortingQuery}, default sorting type will be used.
	 * 
	 * @param aFieldName
	 *            field name
	 * @return {@link SortingQuery}
	 */
	public static SortingQuery create(final String aFieldName) {
		return create(aFieldName, SortingType.DEFAULT);
	}

	/**
	 * Create new instance of {@link SortingQuery}.
	 * 
	 * @param aFieldName
	 *            field name
	 * @param sortingType
	 *            sorting type
	 * @return {@link SortingQuery}
	 */
	public static SortingQuery create(final String aFieldName, final SortingType sortingType) {
		return new SortingQuery(aFieldName, QuerySortingValue.create(sortingType != null ? sortingType : SortingType.DEFAULT));
	}

	@Override
	public String toString() {
		return "sorting[" + getFieldName() + "=" + getQueryValue() + "]";
	}

}
