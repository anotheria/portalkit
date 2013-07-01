package net.anotheria.portalkit.services.storage.query.common;

import java.lang.reflect.Field;

import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.LimitQuery;
import net.anotheria.portalkit.services.storage.query.OffsetQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.util.EntityUtils;

/**
 * {@link Query} utilities.
 * 
 * @author Alexandr Bolbat
 */
public final class QueryUtils {

	/**
	 * Default constructor.
	 */
	private QueryUtils() {
		throw new IllegalAccessError("Can't be instantiated.");
	}

	/**
	 * Get bean value.
	 * 
	 * @param bean
	 *            bean
	 * @param fieldName
	 *            bean field name
	 * @return value
	 */
	public static Object getValue(final Object bean, final String fieldName) {
		if (bean == null || fieldName == null || fieldName.trim().isEmpty())
			return null;

		try {
			int index = fieldName.indexOf(QueryConstants.QUERY_NESTING_SEPARATOR);
			if (index != -1) {
				String nestedObjectFieldName = fieldName.substring(0, index);
				Field nestedBeanField = EntityUtils.getField(bean, nestedObjectFieldName);
				nestedBeanField.setAccessible(true);
				return getValue(nestedBeanField.get(bean), fieldName.substring(index + 1));
			}

			Field beanField = EntityUtils.getField(bean, fieldName);
			beanField.setAccessible(true);
			return beanField.get(bean);
		} catch (SecurityException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (StorageException e) {
			return null;
		}
	}

	/**
	 * Is query are for filtering purposes.<br>
	 * This method mostly used to identify what queries should be excluded from filtering phase.<br>
	 * This approach can be changed in future.
	 * 
	 * @param query
	 *            {@link Query} instance, can't be <code>null</code>
	 * @return <code>true</code> if query are for filtering or <code>false</code>
	 */
	public static boolean isFilteringQuery(final Query query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		if (query instanceof OffsetQuery)
			return true;
		if (query instanceof LimitQuery)
			return true;
		if (query instanceof SortingQuery)
			return true;

		return true;
	}

	/**
	 * Get {@link OffsetQuery} from {@link Query}.
	 * 
	 * @param query
	 *            {@link Query}
	 * @return {@link OffsetQuery} or <code>null</code>
	 */
	public static OffsetQuery getOffset(final Query query) {
		if (query instanceof OffsetQuery)
			return OffsetQuery.class.cast(query);

		if (query instanceof CompositeQuery) {
			CompositeQuery compositeQuery = CompositeQuery.class.cast(query);
			for (Query innerQuery : compositeQuery.getQueryValue().getQueries())
				if (innerQuery instanceof OffsetQuery)
					return OffsetQuery.class.cast(innerQuery);
		}

		return null;
	}

	/**
	 * Get {@link LimitQuery} from {@link Query}.
	 * 
	 * @param query
	 *            {@link Query}
	 * @return {@link LimitQuery} or <code>null</code>
	 */
	public static LimitQuery getLimit(final Query query) {
		if (query instanceof LimitQuery)
			return LimitQuery.class.cast(query);

		if (query instanceof CompositeQuery) {
			CompositeQuery compositeQuery = CompositeQuery.class.cast(query);
			for (Query innerQuery : compositeQuery.getQueryValue().getQueries())
				if (innerQuery instanceof LimitQuery)
					return LimitQuery.class.cast(innerQuery);
		}

		return null;
	}

	/**
	 * Get {@link SortingQuery} from {@link Query}.
	 * 
	 * @param query
	 *            {@link Query}
	 * @return {@link SortingQuery} or <code>null</code>
	 */
	public static SortingQuery getSorting(final Query query) {
		if (query instanceof SortingQuery)
			return SortingQuery.class.cast(query);

		if (query instanceof CompositeQuery) {
			CompositeQuery compositeQuery = CompositeQuery.class.cast(query);
			for (Query innerQuery : compositeQuery.getQueryValue().getQueries())
				if (innerQuery instanceof SortingQuery)
					return SortingQuery.class.cast(innerQuery);
		}

		return null;
	}

}
