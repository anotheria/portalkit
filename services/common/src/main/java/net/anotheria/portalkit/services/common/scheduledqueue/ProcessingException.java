package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * {@link ScheduledQueue} exception, it can be thrown by {@link Processor} on any process exception.
 * 
 * @author Alexandr Bolbat
 */
public class ProcessingException extends ScheduledQueueException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -4422427140141977661L;

	/**
	 * Default constructor.
	 */
	public ProcessingException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 */
	public ProcessingException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param cause
	 *            - exception cause
	 */
	public ProcessingException(final Throwable cause) {
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
	public ProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
