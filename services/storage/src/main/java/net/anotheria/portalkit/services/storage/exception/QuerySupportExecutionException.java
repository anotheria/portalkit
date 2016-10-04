package net.anotheria.portalkit.services.storage.exception;

import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.support.QuerySupport;

/**
 * Runtime exception if {@link QuerySupport} can't execute given {@link Query}.
 * 
 * @author Alexandr Bolbat
 */
public class QuerySupportExecutionException extends QueryRuntimeException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = 5069897164782793270L;

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public <T extends Query> QuerySupportExecutionException(final String message) {
		super(message);
	}

}
