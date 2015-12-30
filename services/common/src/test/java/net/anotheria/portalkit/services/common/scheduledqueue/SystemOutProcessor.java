package net.anotheria.portalkit.services.common.scheduledqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link Processor} testing implementation.
 * 
 * @author Alexandr Bolbat
 */
public class SystemOutProcessor implements Processor {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemOutProcessor.class);

	/**
	 * Processed elements amount.
	 */
	private final AtomicInteger processed = new AtomicInteger(0);

	@Override
	public void process(final Object element) throws ProcessingException {
		LOGGER.info("Element[" + element + "], processed at: " + System.currentTimeMillis());
		processed.incrementAndGet();
	}

	public int getProcessed() {
		return processed.get();
	}

}
