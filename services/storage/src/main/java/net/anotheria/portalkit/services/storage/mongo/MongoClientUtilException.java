package net.anotheria.portalkit.services.storage.mongo;

import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;

/**
 * {@link MongoClientUtil} exception.
 * 
 * @author Alexandr Bolbat
 */
public class MongoClientUtilException extends StorageRuntimeException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -4408607749919312299L;

	/**
	 * Default constructor.
	 */
	public MongoClientUtilException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public MongoClientUtilException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public MongoClientUtilException(final Throwable cause) {
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
	public MongoClientUtilException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
