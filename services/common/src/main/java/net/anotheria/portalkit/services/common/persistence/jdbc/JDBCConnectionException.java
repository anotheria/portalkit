package net.anotheria.portalkit.services.common.persistence.jdbc;

/**
 * Connection that is thrown if something went wrong during connection establishment. 
 * @author lrosenberg
 *
 */
public class JDBCConnectionException extends RuntimeException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 2856635701712204716L;

	/**
	 * Creates a new JDBCConnectionException.
	 */
	public JDBCConnectionException() {
		super("Database connection problem.");
	}

	/**
	 * Creates a new JDBCConnectionException.
	 * @param message	error message.
	 */
	public JDBCConnectionException(String message) {
		super(message);
	}

	/**
	 * Creates a new JDBCConnectionException.
	 * @param message	error message.
	 * @param cause		exception cause.
	 */
	public JDBCConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}