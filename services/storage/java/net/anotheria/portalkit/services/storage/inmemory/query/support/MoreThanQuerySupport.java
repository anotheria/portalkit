package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.exception.QuerySupportExecutionException;
import net.anotheria.portalkit.services.storage.query.MoreThanModifier;
import net.anotheria.portalkit.services.storage.query.MoreThanQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;
import net.anotheria.portalkit.services.storage.util.NumberUtils;

/**
 * {@link MoreThanQuery} in-memory support.
 * 
 * @author Alexandr Bolbat
 */
public class MoreThanQuerySupport extends InMemoryQuerySupport {

	@Override
	public boolean canPass(final Query query, final Serializable bean) {
		if (!(query instanceof MoreThanQuery))
			throw new QuerySupportExecutionException("Can't execute [" + this.toString() + "] with query[" + query + "].");

		MoreThanQuery moreThanQuery = MoreThanQuery.class.cast(query);
		String fieldName = moreThanQuery.getFieldName();
		Serializable queryValue = moreThanQuery.getQueryValue().getValue();
		Object beanValue = QueryUtils.getValue(bean, fieldName);

		if (!(beanValue instanceof Number) || !(queryValue instanceof Number)) // if bean value or queryValue not instance of number
			return false;

		Number queryNumber = Number.class.cast(queryValue);
		Number beanNumber = Number.class.cast(beanValue);

		int comparationResult = NumberUtils.compare(beanNumber, queryNumber);
		return moreThanQuery.getModifier() == MoreThanModifier.MORE ? comparationResult > 0 : comparationResult >= 0;
	}
}
