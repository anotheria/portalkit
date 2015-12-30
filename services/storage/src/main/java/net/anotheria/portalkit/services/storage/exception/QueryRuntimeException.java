package net.anotheria.portalkit.services.storage.exception;

/**
 * General query functionality runtime exception.
 * 
 * @author Alexandr Bolbat
 */
public class QueryRuntimeException extends StorageRuntimeException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -7540853610096278538L;

	/**
	 * Default constructor.
	 */
	public QueryRuntimeException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public QueryRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public QueryRuntimeException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 * @param cause
	 *            exception cause
	 */
	public QueryRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
