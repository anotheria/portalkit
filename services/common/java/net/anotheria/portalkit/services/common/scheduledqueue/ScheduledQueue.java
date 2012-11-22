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
	 * @throws ScheduledQueueException
	 */
	void pause() throws ScheduledQueueException;

	/**
	 * Resume scheduled loading elements.
	 * 
	 * @throws ScheduledQueueException
	 */
	void resume() throws ScheduledQueueException;

	/**
	 * Configure scheduler with new schedule based on cron configuration, paused scheduler resume it's work with new schedule.
	 * 
	 * @param schedule
	 *            - cron based schedule
	 * @throws ScheduledQueueException
	 */
	void schedule(String schedule) throws ScheduledQueueException;

	/**
	 * Configure scheduler with new schedule based on repeat interval, paused scheduler resume it's work with new schedule.
	 * 
	 * @param interval
	 *            - repeat interval in milliseconds
	 * @throws ScheduledQueueException
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
