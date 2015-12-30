package net.anotheria.portalkit.services.storage.exception;

import net.anotheria.portalkit.services.storage.query.Query;

/**
 * Runtime exception if {@link QuerySupport} not configured for required {@link Query}.
 * 
 * @author Alexandr Bolbat
 */
public class QuerySupportConfiguredException extends QueryRuntimeException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -5459130506217843360L;

	/**
	 * Public constructor.
	 * 
	 * @param queryType
	 *            {@link Query} type
	 */
	public <T extends Query> QuerySupportConfiguredException(final Class<T> queryType) {
		super("Query[" + queryType + "] support configured.");
	}

}
