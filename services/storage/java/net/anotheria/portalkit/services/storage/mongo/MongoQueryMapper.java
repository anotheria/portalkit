package net.anotheria.portalkit.services.storage.mongo;

import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.query.BetweenModifier;
import net.anotheria.portalkit.services.storage.query.BetweenQuery;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.ContainsQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.LessThanModifier;
import net.anotheria.portalkit.services.storage.query.LessThanQuery;
import net.anotheria.portalkit.services.storage.query.LimitQuery;
import net.anotheria.portalkit.services.storage.query.MoreThanModifier;
import net.anotheria.portalkit.services.storage.query.MoreThanQuery;
import net.anotheria.portalkit.services.storage.query.OffsetQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.query.SortingType;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import net.anotheria.portalkit.services.storage.query.value.NullValue;

/**
 * Utility for converting {@link Query} to mongo query.
 * 
 * @author Alexandr Bolbat
 */
public final class MongoQueryMapper {

	/**
	 * Default constructor.
	 */
	private MongoQueryMapper() {
		throw new IllegalAccessError();
	}

	/**
	 * Map {@link Query} to mongo query format.
	 * 
	 * @param query
	 *            {@link Query}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 * @throws StorageException
	 */
	public static BasicDBObject map(final Query query) throws StorageException {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		if (query instanceof CompositeQuery) {
			CompositeQuery compositeQuery = CompositeQuery.class.cast(query);
			String modifier = compositeQuery.getModifier() == CompositeModifier.AND ? QueryOperators.AND : QueryOperators.OR;

			BasicDBList compositePart = new BasicDBList();
			for (Query innerQuery : compositeQuery.getQueryValue().getQueries()) {
				BasicDBObject innerPart = map(innerQuery);
				if (innerPart == null)
					continue;

				compositePart.add(innerPart);
			}

			if (compositePart.isEmpty())
				return new BasicDBObject();

			return new BasicDBObject(modifier, compositePart);
		}

		if (query instanceof OffsetQuery)
			return null;

		if (query instanceof LimitQuery)
			return null;

		if (query instanceof SortingQuery)
			return null;

		if (query instanceof EqualQuery)
			return map(EqualQuery.class.cast(query));

		if (query instanceof MoreThanQuery)
			return map(MoreThanQuery.class.cast(query));

		if (query instanceof LessThanQuery)
			return map(LessThanQuery.class.cast(query));

		if (query instanceof BetweenQuery)
			return map(BetweenQuery.class.cast(query));

		if (query instanceof ContainsQuery)
			return map(ContainsQuery.class.cast(query));

		// TODO Implement CustomQuery support

		throw new StorageException("Not supported query[" + query + "].");
	}

	/**
	 * Map {@link EqualQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link EqualQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final EqualQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");
		if(query.getQueryValue() instanceof NullValue)
			return new BasicDBObject(query.getFieldName(), Boolean.TRUE.equals(query.getQueryValue().getValue()) ? null : new BasicDBObject("$ne", null));

		return new BasicDBObject(query.getFieldName(), query.getQueryValue().getValue());
	}

	/**
	 * Map {@link MoreThanQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link MoreThanQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final MoreThanQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		String modifier = query.getModifier() == MoreThanModifier.MORE ? QueryOperators.GT : QueryOperators.GTE;
		return new BasicDBObject(query.getFieldName(), new BasicDBObject(modifier, query.getQueryValue().getValue()));
	}

	/**
	 * Map {@link LessThanQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link LessThanQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final LessThanQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		String modifier = query.getModifier() == LessThanModifier.LESS ? QueryOperators.LT : QueryOperators.LTE;
		return new BasicDBObject(query.getFieldName(), new BasicDBObject(modifier, query.getQueryValue().getValue()));
	}

	/**
	 * Map {@link BetweenQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link BetweenQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final BetweenQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		String modifierGT = query.getModifier() == BetweenModifier.EXCLUDING ? QueryOperators.GT : QueryOperators.GTE;
		String modifierLT = query.getModifier() == BetweenModifier.EXCLUDING ? QueryOperators.LT : QueryOperators.LTE;
		BasicDBObject betweenPart = new BasicDBObject(modifierGT, query.getPairValue().getFirstValue());
		betweenPart.append(modifierLT, query.getPairValue().getSecondValue());
		return new BasicDBObject(query.getFieldName(), betweenPart);
	}

	/**
	 * Map {@link ContainsQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link ContainsQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final ContainsQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		BasicDBObject containsPart = new BasicDBObject(QueryOperators.IN, query.getQueryValue().getValues());
		return new BasicDBObject(query.getFieldName(), containsPart);
	}

	/**
	 * Map {@link SortingQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link SortingQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final SortingQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		int modifier = query.getQueryValue().getValue() == SortingType.ASC ? 1 : -1;
		return new BasicDBObject(query.getFieldName(), modifier);
	}

	/**
	 * Get Mongo sorting query in {@link BasicDBObject} representation from {@link Query}.
	 * 
	 * @param query
	 *            {@link Query}
	 * @return {@link BasicDBObject} or <code>null</code>
	 */
	public static BasicDBObject getSorting(final Query query) {
		SortingQuery sortingQuery = QueryUtils.getSorting(query);
		return sortingQuery != null ? map(sortingQuery) : null;
	}

}
