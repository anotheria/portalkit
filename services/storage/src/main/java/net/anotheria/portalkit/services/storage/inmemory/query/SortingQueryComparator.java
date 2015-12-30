package net.anotheria.portalkit.services.storage.inmemory.query;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.query.SortingType;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;
import net.anotheria.portalkit.services.storage.util.NumberUtils;

/**
 * {@link SortingQuery} comparator.
 * 
 * @author Alexandr Bolbat
 */
public final class SortingQueryComparator implements Comparator<Serializable>, Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 3816252335759956067L;

	/**
	 * Comparison configuration.
	 */
	private final SortingQuery sortingQuery;

	/**
	 * Default constructor.
	 * 
	 * @param aSortingQuery
	 *            comparison configuration, can't be <code>null</code>
	 */
	public SortingQueryComparator(final SortingQuery aSortingQuery) {
		if (aSortingQuery == null)
			throw new IllegalArgumentException("aSortingQuery argument is null.");

		this.sortingQuery = aSortingQuery;
	}

	@Override
	public int compare(final Serializable first, final Serializable second) {
		if (first == null && second == null)
			return 0;

		final int result = sortingQuery.getQueryValue().getValue() == SortingType.ASC ? 1 : -1;
		if (first != null && second == null)
			return result;
		if (first == null && second != null)
			return -result;

		Object firstValue = QueryUtils.getValue(first, sortingQuery.getFieldName());
		Object secondValue = QueryUtils.getValue(second, sortingQuery.getFieldName());
		if (firstValue == null && secondValue == null)
			return 0;
		if (firstValue != null && secondValue == null)
			return result;
		if (firstValue == null && secondValue != null)
			return -result;

		int compareResult = 0;
		if (firstValue instanceof Number && secondValue instanceof Number) { // number comparison
			compareResult = NumberUtils.compare(Number.class.cast(firstValue), Number.class.cast(secondValue));
		} else if (firstValue instanceof Date && secondValue instanceof Date) { // date comparison
			Date firstDate = Date.class.cast(firstValue);
			Date secondDate = Date.class.cast(secondValue);
			compareResult = firstDate.compareTo(secondDate);
		} else { // string comparison for other types
			compareResult = String.valueOf(firstValue).compareTo(String.valueOf(secondValue));
		}

		if (compareResult == 0)
			return compareResult;

		return sortingQuery.getQueryValue().getValue() == SortingType.ASC ? compareResult : -compareResult;
	}

}
