package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import net.anotheria.portalkit.services.storage.exception.QuerySupportExecutionException;
import net.anotheria.portalkit.services.storage.query.ContainsQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;

/**
 * {@link ContainsQuery} in-memory support.
 * 
 * @author Alexandr Bolbat
 */
public class ContainsQuerySupport extends InMemoryQuerySupport {

	// performance of current implementation should be reviewed and improved
	@Override
	public boolean canPass(final Query query, final Serializable bean) {
		if (!(query instanceof ContainsQuery))
			throw new QuerySupportExecutionException("Can't execute [" + this.toString() + "] with query[" + query + "].");

		ContainsQuery containsQuery = ContainsQuery.class.cast(query);
		String fieldName = query.getFieldName();
		List<Serializable> queryValues = containsQuery.getQueryValue().getValues();
		Object beanValue = QueryUtils.getValue(bean, fieldName);

		if (beanValue == null || queryValues == null || queryValues.size() == 0)
			return false;

		if (beanValue instanceof Collection) {
			Collection<?> beanValues = (Collection<?>) beanValue;
			for (Object value : beanValues) {
				if (value == null)
					continue;

				for (Serializable queryValue : queryValues)
					if (queryValue != null && queryValue.equals(value))
						return true;
			}

			return false;
		}

		if (beanValue.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(beanValue); ++i) {
				Object value = Array.get(beanValue, i);
				if (value == null)
					continue;

				for (Serializable queryValue : queryValues)
					if (queryValue != null && queryValue.equals(value))
						return true;
			}

			return false;
		}

		for (Serializable queryValue : queryValues)
			if (queryValue != null && queryValue.equals(beanValue))
				return true;

		return false;
	}
}
