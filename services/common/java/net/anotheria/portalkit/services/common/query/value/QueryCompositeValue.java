package net.anotheria.portalkit.services.common.query.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.anotheria.portalkit.services.common.query.Query;

/**
 * {@link QueryCompositeValue} {@link QueryValue}.
 * 
 * @author Alexandr Bolbat
 */
public class QueryCompositeValue implements QueryValue {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -6484398818614743471L;

	/**
	 * First {@link Query}.
	 */
	private final List<Query> queries;

	/**
	 * Default constructor.
	 * 
	 * @param aQueries
	 *            queries list
	 */
	public QueryCompositeValue(final List<Query> aQueries) {
		if (aQueries == null)
			throw new IllegalArgumentException("aQueries argument is null");

		this.queries = aQueries;
	}

	@Override
	public QueryCompositeValue getValue() {
		return this;
	}

	public List<Query> getQueries() {
		return queries;
	}

	/**
	 * Create new instance of {@link QueryCompositeValue}.
	 * 
	 * @param aQueries
	 *            queries list
	 * @return {@link QueryCompositeValue}
	 */
	public static QueryCompositeValue create(final Query... aQueries) {
		List<Query> queries = new ArrayList<Query>();
		if (aQueries != null && aQueries.length > 0)
			queries.addAll(Arrays.asList(aQueries));

		return new QueryCompositeValue(queries);
	}

	@Override
	public String toString() {
		return queries.toString();
	}

}
