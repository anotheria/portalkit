package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * Interface for scheduled queue.
 * 
 * @author Alexandr Bolbat
 */
public interface ScheduledQueue {

	/**
	 * Pause scheduled loading elements.
	 * 
	 * @throws ScheduledQueueException	if error.
	 */
	void pause() throws ScheduledQueueException;

	/**
	 * Resume scheduled loading elements.
	 * 
	 * @throws ScheduledQueueException	if error.
	 */
	void resume() throws ScheduledQueueException;

	/**
	 * Is scheduler started.
	 *
	 * @return true if scheduler started.
	 * @throws ScheduledQueueException	if error.
	 */
	boolean isStarted() throws ScheduledQueueException;

	/**
	 * Is scheduler paused.
	 *
	 * @return true if scheduler paused.
	 * @throws ScheduledQueueException	if error.
	 */
	boolean isPaused() throws ScheduledQueueException;

	/**
	 * Configure scheduler with new schedule based on cron configuration, paused scheduler resume it's work with new schedule.
	 * 
	 * @param schedule
	 *            - cron based schedule
	 * @throws ScheduledQueueException 	if error.
	 */
	void schedule(String schedule) throws ScheduledQueueException;

	/**
	 * Configure scheduler with new schedule based on repeat interval, paused scheduler resume it's work with new schedule.
	 * 
	 * @param interval
	 *            - repeat interval in milliseconds
	 * @throws ScheduledQueueException	if error.
	 */
	void schedule(long interval) throws ScheduledQueueException;

	/**
	 * Set mode.
	 * 
	 * @param mode
	 *            - processing mode
	 */
	void setMode(ProcessingMode mode);

	/**
	 * Stop scheduler (loading elements to the queue) and queue (processing elements), future use of this instance is not possible after this action.
	 */
	void tearDown();

}
