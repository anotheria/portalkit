package net.anotheria.portalkit.services.storage.mongo;

import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.mongo.util.MongoConstants;
import net.anotheria.portalkit.services.storage.query.BetweenModifier;
import net.anotheria.portalkit.services.storage.query.BetweenQuery;
import net.anotheria.portalkit.services.storage.query.CompositeModifier;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.LessThenModifier;
import net.anotheria.portalkit.services.storage.query.LessThenQuery;
import net.anotheria.portalkit.services.storage.query.LimitQuery;
import net.anotheria.portalkit.services.storage.query.MoreThenModifier;
import net.anotheria.portalkit.services.storage.query.MoreThenQuery;
import net.anotheria.portalkit.services.storage.query.OffsetQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.SortingQuery;
import net.anotheria.portalkit.services.storage.query.SortingType;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;

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

		if (query instanceof MoreThenQuery)
			return map(MoreThenQuery.class.cast(query));

		if (query instanceof LessThenQuery)
			return map(LessThenQuery.class.cast(query));

		if (query instanceof BetweenQuery)
			return map(BetweenQuery.class.cast(query));

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

		return new BasicDBObject(query.getFieldName(), query.getQueryValue().getValue());
	}

	/**
	 * Map {@link MoreThenQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link MoreThenQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final MoreThenQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		String modifier = query.getModifier() == MoreThenModifier.MORE ? QueryOperators.GT : QueryOperators.GTE;
		return new BasicDBObject(query.getFieldName(), new BasicDBObject(modifier, query.getQueryValue().getValue()));
	}

	/**
	 * Map {@link LessThenQuery} to mongo query format.
	 * 
	 * @param query
	 *            {@link LessThenQuery}, can't be <code>null</code>
	 * @return {@link BasicDBObject}
	 */
	public static BasicDBObject map(final LessThenQuery query) {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		String modifier = query.getModifier() == LessThenModifier.LESS ? QueryOperators.LT : QueryOperators.LTE;
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
		return new BasicDBObject(MongoConstants.OPERATOR_SORTING, new BasicDBObject(query.getFieldName(), modifier));
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
	public static BasicDBObject getSorting(final Query query) {
		if (query instanceof SortingQuery)
			return map(SortingQuery.class.cast(query));

		if (query instanceof CompositeQuery) {
			CompositeQuery compositeQuery = CompositeQuery.class.cast(query);
			for (Query innerQuery : compositeQuery.getQueryValue().getQueries())
				if (innerQuery instanceof SortingQuery)
					return map(SortingQuery.class.cast(innerQuery));
		}

		return null;
	}

}
