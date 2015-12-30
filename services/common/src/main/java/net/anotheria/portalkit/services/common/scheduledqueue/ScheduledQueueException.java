package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * {@link ScheduledQueue} general exception.
 * 
 * @author Alexandr Bolbat
 */
public class ScheduledQueueException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -257541407969395209L;

	/**
	 * Default constructor.
	 */
	public ScheduledQueueException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 */
	public ScheduledQueueException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            - exception cause
	 */
	public ScheduledQueueException(final Throwable cause) {
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
	public ScheduledQueueException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
