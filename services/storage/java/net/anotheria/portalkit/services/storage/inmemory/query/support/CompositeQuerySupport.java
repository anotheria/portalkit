package net.anotheria.portalkit.services.storage.inmemory.query.support;

import java.io.Serializable;
import java.util.List;

import net.anotheria.portalkit.services.storage.exception.QuerySupportExecutionException;
import net.anotheria.portalkit.services.storage.inmemory.query.InMemoryQueryProcessor;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;

/**
 * {@link CompositeQuery} in-memory support.
 * 
 * @author Alexandr Bolbat
 */
public class CompositeQuerySupport extends InMemoryQuerySupport {

	@Override
	public boolean canPass(final Query query, final Serializable bean) {
		if (!(query instanceof CompositeQuery))
			throw new QuerySupportExecutionException("Can't execute [" + this.toString() + "] with query[" + query + "].");

		CompositeQuery compositeQuery = CompositeQuery.class.cast(query);
		CompositeModifier modifier = compositeQuery.getModifier();
		List<Query> queries = compositeQuery.getQueryValue().getQueries();

		if (queries == null || queries.size() == 0)
			return true;

		boolean result = true;
		for (Query subQuery : queries) {
			if (!QueryUtils.isFilteringQuery(subQuery))
				continue;

			result = InMemoryQueryProcessor.executeFiltering(subQuery, bean);
			if (modifier == CompositeModifier.OR && result)
				return true;

			if (modifier == CompositeModifier.OR && !result)
				continue;

			if (!result)
				return false;
		}

		return result;
	}
}
