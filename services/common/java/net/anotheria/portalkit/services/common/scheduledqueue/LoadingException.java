package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * {@link ScheduledQueue} exception, it can be thrown by {@link Loader} on any loading exception.
 * 
 * @author Alexandr Bolbat
 */
public class LoadingException extends ScheduledQueueException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -2974339666645783846L;

	/**
	 * Default constructor.
	 */
	public LoadingException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 */
	public LoadingException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            - exception cause
	 */
	public LoadingException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public LoadingException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
