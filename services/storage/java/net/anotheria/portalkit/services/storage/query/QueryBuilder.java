package net.anotheria.portalkit.services.storage.query;

import net.anotheria.portalkit.services.storage.query.support.QuerySupport;

/**
 * Utility for simple {@link Query} construction.
 * 
 * @author Alexandr Bolbat
 */
public final class QueryBuilder {

	/**
	 * Root scope {@link Query}.
	 */
	private CompositeQuery rootQuery;

	/**
	 * Default constructor.
	 */
	private QueryBuilder(final CompositeModifier modifier) {
		this.rootQuery = CompositeQuery.create(modifier);
	}

	/**
	 * Create new instance of {@link QueryBuilder}.
	 * 
	 * @return {@link QueryBuilder}
	 */
	public static QueryBuilder create() {
		return new QueryBuilder(CompositeModifier.DEFAULT);
	}

	/**
	 * Create new instance of {@link QueryBuilder}.
	 * 
	 * @param modifier
	 *            {@link CompositeModifier}
	 * @return {@link QueryBuilder}
	 */
	public static QueryBuilder create(final CompositeModifier modifier) {
		if (modifier == null)
			throw new IllegalArgumentException("modifier argument is null.");

		return new QueryBuilder(modifier);
	}

	/**
	 * Add {@link CompositeQuery}.
	 * 
	 * @param query
	 *            {@link CompositeQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final CompositeQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Add {@link EqualQuery}.
	 * 
	 * @param query
	 *            {@link EqualQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final EqualQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Add {@link LessThanQuery}.
	 * 
	 * @param query
	 *            {@link LessThanQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final LessThanQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Add {@link MoreThanQuery}.
	 * 
	 * @param query
	 *            {@link MoreThanQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final MoreThanQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Add {@link BetweenQuery}.
	 * 
	 * @param query
	 *            {@link BetweenQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final BetweenQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Add {@link ContainsQuery}.
	 * 
	 * @param query
	 *            {@link ContainsQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final ContainsQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Add {@link CustomQuery}.<br>
	 * {@link QuerySupport} for {@link CustomQuery} should be registered before it's execution.
	 * 
	 * @param query
	 *            {@link CustomQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder add(final CustomQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Set {@link SortingType}.
	 * 
	 * @param query
	 *            {@link SortingQuery}, can't be null
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder setSorting(final SortingQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument is null.");

		rootQuery.add(query);
		return this;
	}

	/**
	 * Set results limit.
	 * 
	 * @param limit
	 *            result entities limit, can't be less than 0
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder setLimit(final int limit) {
		if (limit < 0)
			throw new IllegalArgumentException("limit argument can't be less than 0.");

		rootQuery.add(LimitQuery.create(limit));
		return this;
	}

	/**
	 * Set results offset.
	 * 
	 * @param offset
	 *            result entities offset, can't be less than 0
	 * @return {@link QueryBuilder}
	 */
	public QueryBuilder setOffset(final int offset) {
		if (offset < 0)
			throw new IllegalArgumentException("offset argument can't be less than 0.");

		rootQuery.add(OffsetQuery.create(offset));
		return this;
	}

	/**
	 * Build {@link Query}.
	 * 
	 * @return {@link Query}
	 */
	public Query build() {
		return rootQuery;
	}

}
