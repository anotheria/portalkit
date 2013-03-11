package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.exception.QuerySupportExecutionException;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;

/**
 * {@link EqualQuery} in-memory support.
 * 
 * @author Alexandr Bolbat
 */
public class EqualQuerySupport extends InMemoryQuerySupport {

	@Override
	public boolean canPass(final Query query, final Serializable bean) {
		if (!(query instanceof EqualQuery))
			throw new QuerySupportExecutionException("Can't execute [" + this.toString() + "] with query[" + query + "].");

		EqualQuery equalQuery = EqualQuery.class.cast(query);
		String fieldName = equalQuery.getFieldName();
		Serializable queryValue = equalQuery.getQueryValue().getValue();
		Object beanValue = QueryUtils.getValue(bean, fieldName);

		if (beanValue == null && queryValue == null)
			return true;

		return queryValue != null && beanValue != null ? queryValue.equals(beanValue) : false;
	}

}
