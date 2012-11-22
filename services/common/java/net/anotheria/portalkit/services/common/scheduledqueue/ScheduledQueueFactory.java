package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * {@link ScheduledQueue} factory.
 * 
 * @author Alexandr Bolbat
 */
public final class ScheduledQueueFactory {

	/**
	 * Create instance and configure {@link ScheduledQueue}.
	 * 
	 * @param loader
	 *            - elements loader
	 * @param processor
	 *            - elements processor
	 * @return {@link ScheduledQueue}
	 * @throws ScheduledQueueException
	 */
	public static ScheduledQueue create(final Loader loader, final Processor processor) throws ScheduledQueueException {
		return new ScheduledQueueImpl(loader, processor);
	}

}
