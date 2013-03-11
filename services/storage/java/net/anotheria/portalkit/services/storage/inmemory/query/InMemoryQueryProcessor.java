package net.anotheria.portalkit.services.storage.inmemory.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.anotheria.portalkit.services.storage.inmemory.query.support.BetweenQuerySupport;
import net.anotheria.portalkit.services.storage.inmemory.query.support.CompositeQuerySupport;
import net.anotheria.portalkit.services.storage.inmemory.query.support.EqualQuerySupport;
import net.anotheria.portalkit.services.storage.inmemory.query.support.LessThanQuerySupport;
import net.anotheria.portalkit.services.storage.inmemory.query.support.MoreThanQuerySupport;
import net.anotheria.portalkit.services.storage.query.BetweenQuery;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.LessThanQuery;
import net.anotheria.portalkit.services.storage.query.LimitQuery;
import net.anotheria.portalkit.services.storage.query.MoreThanQuery;
import net.anotheria.portalkit.services.storage.query.OffsetQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;
import net.anotheria.portalkit.services.storage.query.support.QuerySupport;
import net.anotheria.portalkit.services.storage.query.support.QuerySupportRegistry;
import net.anotheria.portalkit.services.storage.query.support.QuerySupportRegistry.QuerySupportHolder;
import net.anotheria.portalkit.services.storage.type.StorageType;
import net.anotheria.portalkit.services.storage.util.Slicer;

/**
 * In-memory {@link Query} processor.
 * 
 * @author Alexandr Bolbat
 */
public final class InMemoryQueryProcessor {

	/**
	 * {@link QuerySupport} storage.
	 */
	private static final QuerySupportHolder SUPPORTS = QuerySupportRegistry.getHolder(StorageType.IN_MEMORY_GENERIC);

	/**
	 * Static initialization.
	 */
	static {
		QuerySupportRegistry.configure(StorageType.IN_MEMORY_GENERIC, EqualQuery.class, new EqualQuerySupport());
		QuerySupportRegistry.configure(StorageType.IN_MEMORY_GENERIC, LessThanQuery.class, new LessThanQuerySupport());
		QuerySupportRegistry.configure(StorageType.IN_MEMORY_GENERIC, MoreThanQuery.class, new MoreThanQuerySupport());
		QuerySupportRegistry.configure(StorageType.IN_MEMORY_GENERIC, BetweenQuery.class, new BetweenQuerySupport());
		QuerySupportRegistry.configure(StorageType.IN_MEMORY_GENERIC, CompositeQuery.class, new CompositeQuerySupport());
	}

	/**
	 * Default constructor.
	 */
	private InMemoryQueryProcessor() {
		throw new IllegalAccessError("Shouldn't be instantiated.");
	}

	/**
	 * Execute {@link Query} on given entities.
	 * 
	 * @param entities
	 *            {@link Collection} of entities
	 * @param query
	 *            {@link Query}
	 * @return processed {@link List} of entities with given {@link Query}
	 */
	public static <T extends Serializable> List<T> execute(final Collection<T> entities, final Query query) {
		final List<T> result = new ArrayList<T>();
		if (entities == null || entities.size() == 0) // if no entities to process
			return result;
		if (query == null) { // if query is null
			result.addAll(entities);
			return result;
		}

		// Executing filtering
		for (T entity : entities)
			if (executeFiltering(query, entity))
				result.add(entity);

		// Executing sorting
		executeSorting(QueryUtils.getSorting(query), result);

		// Executing slicing
		OffsetQuery offsetQuery = QueryUtils.getOffset(query);
		LimitQuery limitQuery = QueryUtils.getLimit(query);
		if (offsetQuery == null && limitQuery == null)
			return result;

		int offset = offsetQuery != null ? offsetQuery.getQueryValue().getValue() : 0;
		int limit = limitQuery != null ? limitQuery.getQueryValue().getValue() : Integer.MAX_VALUE;
		return Slicer.slice(result, offset, limit);
	}

	/**
	 * Execute filtering.
	 * 
	 * @param query
	 *            {@link Query}
	 * @param entity
	 *            entity
	 * @return <code>true</code> if entity pass filtering or <code>false</code>
	 */
	public static <T extends Serializable> boolean executeFiltering(final Query query, final T entity) {
		if (!QueryUtils.isFilteringQuery(query))
			return true;

		return SUPPORTS.get(query.getClass()).canPass(query, entity);
	}

	/**
	 * Execute sorting.
	 * 
	 * @param query
	 *            {@link SortingQuery}
	 * @param entities
	 *            {@link List} of entities
	 */
	public static <T extends Serializable> void executeSorting(final SortingQuery query, final List<T> entities) {
		if (query == null || entities == null || entities.size() == 0)
			return;

		Collections.sort(entities, new SortingQueryComparator(query));
	}

}
