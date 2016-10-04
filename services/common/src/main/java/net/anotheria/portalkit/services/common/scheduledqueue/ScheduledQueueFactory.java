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
	 * @param configFile
	 *            - name of the 'quartz' configuration file
	 * @param loader
	 *            - elements loader
	 * @param processor
	 *            - elements processor
	 * 
	 * @return {@link ScheduledQueue}
	 * @throws ScheduledQueueException	if error.
	 */
	public static ScheduledQueue create(final String configFile, final Loader loader, final Processor processor) throws ScheduledQueueException {
		return new ScheduledQueueImpl(configFile, loader, processor);
	}

}
