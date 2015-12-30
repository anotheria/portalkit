package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.io.Serializable;

import net.anotheria.portalkit.services.storage.exception.QuerySupportExecutionException;
import net.anotheria.portalkit.services.storage.query.BetweenModifier;
import net.anotheria.portalkit.services.storage.query.BetweenQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;
import net.anotheria.portalkit.services.storage.util.NumberUtils;

/**
 * {@link BetweenQuery} in-memory support.
 * 
 * @author Alexandr Bolbat
 */
public class BetweenQuerySupport extends InMemoryQuerySupport {

	@Override
	public boolean canPass(final Query query, final Serializable bean) {
		if (!(query instanceof BetweenQuery))
			throw new QuerySupportExecutionException("Can't execute [" + this.toString() + "] with query[" + query + "].");

		BetweenQuery betweenQuery = BetweenQuery.class.cast(query);
		String fieldName = query.getFieldName();
		Serializable firstQueryValue = betweenQuery.getPairValue().getFirstValue().getValue();
		Serializable secondQueryValue = betweenQuery.getPairValue().getSecondValue().getValue();
		Object beanValue = QueryUtils.getValue(bean, fieldName);

		if (!(beanValue instanceof Number) || !(firstQueryValue instanceof Number) || !(secondQueryValue instanceof Number))
			return false;

		Number firstQueryNumber = Number.class.cast(firstQueryValue);
		Number secondQueryNumber = Number.class.cast(secondQueryValue);
		Number beanNumber = Number.class.cast(beanValue);

		int firstCompareResult = NumberUtils.compare(beanNumber, firstQueryNumber);
		int secondCompareResult = NumberUtils.compare(beanNumber, secondQueryNumber);
		if (betweenQuery.getModifier() == BetweenModifier.EXCLUDING)
			return firstCompareResult > 0 && secondCompareResult < 0;

		return firstCompareResult >= 0 && secondCompareResult <= 0;
	}
}
