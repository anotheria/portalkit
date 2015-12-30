package net.anotheria.portalkit.services.storage.query.value;

import net.anotheria.portalkit.services.storage.query.SortingType;

/**
 * {@link QuerySortingValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class QuerySortingValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -7386878462813831141L;

	/**
	 * {@link SortingType} value.
	 */
	private final SortingType sortingType;

	/**
	 * Default constructor.
	 * 
	 * @param aSortingType
	 *            sorting type value
	 */
	public QuerySortingValue(final SortingType aSortingType) {
		if (aSortingType == null)
			throw new IllegalArgumentException("aSortingType argument is null");

		this.sortingType = aSortingType;
	}

	@Override
	public SortingType getValue() {
		return sortingType;
	}

	/**
	 * Create new instance of {@link QuerySortingValue}.
	 * 
	 * @return {@link QuerySortingValue}
	 */
	public static QuerySortingValue create() {
		return new QuerySortingValue(SortingType.DEFAULT);
	}

	/**
	 * Create new instance of {@link QuerySortingValue}.
	 * 
	 * @param aSortingType
	 *            sorting type value
	 * @return {@link QuerySortingValue}
	 */
	public static QuerySortingValue create(final SortingType aSortingType) {
		return new QuerySortingValue(aSortingType != null ? aSortingType : SortingType.DEFAULT);
	}

	@Override
	public String toString() {
		return sortingType.toString();
	}

}
