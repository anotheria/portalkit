package net.anotheria.portalkit.services.common.scheduledqueue;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ScheduledQueue} test.
 * 
 * @author Alexandr Bolbat
 */
public class ScheduledQueueTest {

	/**
	 * Testing {@link ScheduledQueue} instance.
	 */
	private ScheduledQueue queue;

	/**
	 * Test loader. It's generating elements on the fly.
	 */
	private RandomGenerationLoader loader;

	/**
	 * Testing processor. It's counting incoming elements and writing it's to system out.
	 */
	private SystemOutProcessor processor;

	/**
	 * Initialization executed before each test.
	 * 
	 * @throws ScheduledQueueException
	 */
	@Before
	public void before() throws ScheduledQueueException {
		loader = new RandomGenerationLoader();
		processor = new SystemOutProcessor();
		queue = ScheduledQueueFactory.create("quartz.properties", loader, processor);
	}

	/**
	 * Initialization executed after each test.
	 */
	@After
	public void after() {
		queue.tearDown();
	}

	/**
	 * Complex test.
	 * 
	 * @throws InterruptedException
	 * @throws ScheduledQueueException
	 */
	@Test
	public void complexTestForSyncMode() throws ScheduledQueueException, InterruptedException {
		queue.setMode(ProcessingMode.SYNC);
		queue.schedule(1L);

		Thread.sleep(150L);
		Assert.assertEquals("Loaded and processed elements amount should be the same.", loader.getLoaded(), processor.getProcessed());
	}

}
