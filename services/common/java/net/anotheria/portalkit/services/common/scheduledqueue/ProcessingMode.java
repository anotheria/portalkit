package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * {@link ScheduledQueue} processing mode.
 * 
 * @author Alexandr Bolbat
 */
public enum ProcessingMode {

	/**
	 * Asynchronous mode.
	 */
	ASYNC,

	/**
	 * Synchronous mode.
	 */
	SYNC;

	/**
	 * Default mode.
	 */
	public static final ProcessingMode DEFAULT = SYNC;

}
