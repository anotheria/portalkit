package net.anotheria.portalkit.services.common.scheduledqueue;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * {@link Processor} testing implementation.
 * 
 * @author Alexandr Bolbat
 */
public class SystemOutProcessor implements Processor {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(SystemOutProcessor.class);

	/**
	 * Processed elements amount.
	 */
	private static final AtomicInteger processed = new AtomicInteger(0);

	@Override
	public void process(final Object element) throws ProcessingException {
		LOGGER.info("Element[" + element + "], processed at: " + System.currentTimeMillis());
		processed.incrementAndGet();
	}

	public int getProcessed() {
		return processed.get();
	}

}
