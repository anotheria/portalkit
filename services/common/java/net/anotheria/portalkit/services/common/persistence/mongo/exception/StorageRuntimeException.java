package net.anotheria.portalkit.services.common.persistence.mongo.exception;

/**
 * General storage runtime exception.
 * 
 * @author Alexandr Bolbat
 */
public class StorageRuntimeException extends RuntimeException {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -6902189589625442455L;

	/**
	 * Default constructor.
	 */
	public StorageRuntimeException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public StorageRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public StorageRuntimeException(final Throwable cause) {
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
	public StorageRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
