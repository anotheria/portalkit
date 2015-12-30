package net.anotheria.portalkit.services.storage.query.support;

import java.util.HashMap;
import java.util.Map;

import net.anotheria.portalkit.services.storage.exception.QuerySupportConfiguredException;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.type.StorageType;

/**
 * {@link QuerySupport} registry.
 * 
 * @author Alexandr Bolbat
 */
public final class QuerySupportRegistry {

	/**
	 * {@link QuerySupportRegistry} storage.
	 */
	private static final Map<StorageType, QuerySupportHolder> STORAGE = new HashMap<StorageType, QuerySupportHolder>();

	/**
	 * Synchronization lock.
	 */
	private static final Object LOCK = new Object();

	/**
	 * Default constructor.
	 */
	private QuerySupportRegistry() {
		throw new IllegalAccessError("Shouldn't be instantiated.");
	}

	/**
	 * Get {@link QuerySupportHolder} for given {@link StorageType}.
	 * 
	 * @param storageType
	 *            {@link StorageType}, can't be <code>null</code>
	 * @return {@link QuerySupportHolder} instance
	 */
	public static QuerySupportHolder getHolder(final StorageType storageType) {
		if (storageType == null)
			throw new IllegalArgumentException("storageType argument is null.");

		QuerySupportHolder holder = STORAGE.get(storageType);
		if (holder == null) {
			synchronized (LOCK) {
				holder = STORAGE.get(storageType);
				if (holder == null) {
					holder = new QuerySupportHolder();
					STORAGE.put(storageType, holder);
				}
			}
		}

		return holder;
	}

	/**
	 * Get {@link QuerySupport} for given {@link StorageType} and {@link Query}.
	 * 
	 * @param storageType
	 *            {@link StorageType}, can't be <code>null</code>
	 * @param queryType
	 *            {@link Query} type, can't be <code>null</code>
	 * @return {@link QuerySupport} instance
	 */
	public static <T extends Query> QuerySupport getSupport(final StorageType storageType, final Class<T> queryType) {
		if (storageType == null)
			throw new IllegalArgumentException("storageType argument is null.");
		if (queryType == null)
			throw new IllegalArgumentException("queryType argument is null.");

		return getHolder(storageType).get(queryType);
	}

	/**
	 * Configure {@link QuerySupport} for given {@link StorageType} and {@link Query}.
	 * 
	 * @param storageType
	 *            {@link StorageType}, can't be <code>null</code>
	 * @param queryType
	 *            {@link Query} type, can't be <code>null</code>
	 * @param querySupport
	 *            {@link QuerySupport}, can't be <code>null</code>
	 */
	public static <T extends Query> void configure(final StorageType storageType, final Class<T> queryType, final QuerySupport querySupport) {
		if (storageType == null)
			throw new IllegalArgumentException("storageType argument is null.");
		if (queryType == null)
			throw new IllegalArgumentException("queryType argument is null.");
		if (querySupport == null)
			throw new IllegalArgumentException("querySupport argument is null.");

		getHolder(storageType).configure(queryType, querySupport);
	}

	/**
	 * Configuration holder.
	 * 
	 * @author Alexandr Bolbat
	 */
	public static class QuerySupportHolder {

		/**
		 * {@link QuerySupportHolder} storage.
		 */
		private final Map<Class<?>, QuerySupport> storage = new HashMap<Class<?>, QuerySupport>();

		/**
		 * Get configured {@link QuerySupport} for {@link Query}.
		 * 
		 * @param queryType
		 *            {@link Query} type
		 * @return {@link QuerySupport}
		 */
		public <T extends Query> QuerySupport get(final Class<T> queryType) {
			if (queryType == null)
				throw new IllegalArgumentException("queryType argument is null.");

			QuerySupport result = storage.get(queryType);
			if (result == null)
				throw new QuerySupportConfiguredException(queryType);

			return result;
		}

		/**
		 * Configure {@link QuerySupport} for {@link Query}.
		 * 
		 * @param queryType
		 *            {@link Query} type
		 * @param querySupport
		 *            {@link QuerySupport}
		 */
		private synchronized <T extends Query> void configure(final Class<T> queryType, final QuerySupport querySupport) {
			if (queryType == null)
				throw new IllegalArgumentException("queryType argument is null.");
			if (querySupport == null)
				throw new IllegalArgumentException("querySupport argument is null.");

			storage.put(queryType, querySupport);
		}

	}

}
