package net.anotheria.portalkit.services.storage.exception;

/**
 * General storage exception.
 * 
 * @author Alexandr Bolbat
 */
public class StorageException extends Exception {

	/**
	 * Generated SerialVersionUID.
	 */
	private static final long serialVersionUID = -1718584286520893129L;

	/**
	 * Default constructor.
	 */
	public StorageException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public StorageException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            exception cause
	 */
	public StorageException(final Throwable cause) {
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
	public StorageException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
