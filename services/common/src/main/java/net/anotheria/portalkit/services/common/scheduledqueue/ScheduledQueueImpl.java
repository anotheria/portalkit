package net.anotheria.portalkit.services.common.scheduledqueue;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ScheduledQueue} implementation.
 * 
 * @author Alexandr Bolbat
 */
public class ScheduledQueueImpl implements ScheduledQueue {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledQueueImpl.class);

	/**
	 * Processing mode.
	 */
	private volatile ProcessingMode mode = ProcessingMode.DEFAULT;

	/**
	 * Configured scheduler.
	 */
	private volatile Scheduler scheduler;

	/**
	 * Configured job.
	 */
	private volatile JobDetail schedulerJob;

	/**
	 * Configured elements {@link Loader} instance.
	 */
	private Loader loader;

	/**
	 * Configured elements {@link Processor} instance.
	 */
	private Processor processor;

	/**
	 * Synchronization lock.
	 */
	private final Object lock = new Object();

	/**
	 * Default constructor.
	 * 
	 * @param configFile
	 *            - name of the 'quartz' configuration file
	 * @param aLoader
	 *            - elements loader
	 * @param aProcessor
	 *            - elements processor
	 * @throws ScheduledQueueException	if error.
	 */
	protected ScheduledQueueImpl(final String configFile, final Loader aLoader, final Processor aProcessor) throws ScheduledQueueException {
		if (aLoader == null)
			throw new IllegalArgumentException("aLoader argument is null");
		if (aProcessor == null)
			throw new IllegalArgumentException("aProcessor argument is null");

		this.loader = aLoader;
		this.processor = aProcessor;

		configureQueue();

		// scheduler initialization
		try {
			scheduler = new StdSchedulerFactory(configFile).getScheduler();
			scheduler.start();

			schedulerJob = JobBuilder.newJob(LoaderJob.class).withIdentity("LoaderJob", "ScheduledQueue").build();
			schedulerJob.getJobDataMap().put(LoaderJob.LOADER, loader);
			schedulerJob.getJobDataMap().put(LoaderJob.PROCESSOR, processor);
			schedulerJob.getJobDataMap().put(LoaderJob.PROCESSING_MODE, mode);
		} catch (SchedulerException e) {
			String message = "ScheduledQueueImpl(...) scheduler initialization fail.";
			LOGGER.error(message, e);
			throw new ScheduledQueueException(message, e);
		}
	}

	/**
	 * Queue configuration.
	 */
	private void configureQueue() {

	}

	/**
	 * Trigger configuration.
	 * 
	 * @param trigger
	 *            - configured trigger
	 * 
	 * @throws ScheduledQueueException
	 */
	private synchronized void configureTrigger(final Trigger trigger) throws ScheduledQueueException {
		try {
			// clearing old triggers from scheduler
			pause();
			if (scheduler.checkExists(schedulerJob.getKey()))
				scheduler.deleteJob(schedulerJob.getKey());

			scheduler.scheduleJob(schedulerJob, trigger);
			resume();
		} catch (SchedulerException e) {
			String message = "configureTrigger(" + trigger + ") fail";
			LOGGER.error(message, e);
			throw new ScheduledQueueException(message, e);
		}
	}

	@Override
	public void pause() throws ScheduledQueueException {
		synchronized (lock) {
			try {
				if (scheduler.isShutdown())
					throw new IllegalStateException("Scheduler is off");

				if (scheduler.isStarted())
					scheduler.standby();
			} catch (SchedulerException e) {
				String message = "pause() fail";
				LOGGER.error(message, e);
				throw new ScheduledQueueException(message, e);
			}
		}
	}

	@Override
	public void resume() throws ScheduledQueueException {
		synchronized (lock) {
			try {
				if (scheduler.isShutdown())
					throw new IllegalStateException("Scheduler is off");

				if (scheduler.isInStandbyMode())
					scheduler.start();
			} catch (SchedulerException e) {
				String message = "resume() fail";
				LOGGER.error(message, e);
				throw new ScheduledQueueException(message, e);
			}
		}
	}

	@Override
	public boolean isStarted() throws ScheduledQueueException {
		try {
			return !scheduler.isShutdown();
		} catch (SchedulerException e) {
			String message = "isStarted() fail";
			LOGGER.error(message, e);
			throw new ScheduledQueueException(message, e);
		}
	}

	@Override
	public boolean isPaused() throws ScheduledQueueException {
		try {
			return scheduler.isInStandbyMode();
		} catch (SchedulerException e) {
			String message = "isStarted() fail";
			LOGGER.error(message, e);
			throw new ScheduledQueueException(message, e);
		}
	}

	@Override
	public void schedule(final String schedule) throws ScheduledQueueException {
		try {
			if (scheduler.isShutdown())
				throw new IllegalStateException("Scheduler is off");

			if (schedule == null || schedule.trim().isEmpty())
				return;

			TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger();
			tb.withIdentity("LoaderTrigger", "ScheduledQueue").startNow();
			tb.withSchedule(CronScheduleBuilder.cronSchedule(schedule));
			tb.forJob(schedulerJob.getKey());
			configureTrigger(tb.build());
		} catch (SchedulerException e) {
			String message = "schedule(" + schedule + ") fail";
			LOGGER.error(message, e);
			throw new ScheduledQueueException(message, e);
		}
	}

	@Override
	public void schedule(long interval) throws ScheduledQueueException {
		try {
			if (scheduler.isShutdown())
				throw new IllegalStateException("Scheduler is off");

			if (interval < 1)
				throw new IllegalArgumentException("interval argument should be more then 0");

			TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger();
			tb.withIdentity("LoaderTrigger", "ScheduledQueue").startNow();
			tb.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).repeatForever());
			configureTrigger(tb.build());
		} catch (SchedulerException e) {
			String message = "schedule(" + interval + ") fail";
			LOGGER.error(message, e);
			throw new ScheduledQueueException(message, e);
		}
	}

	@Override
	public synchronized void setMode(final ProcessingMode aMode) {
		if (aMode == null)
			throw new IllegalArgumentException("aProcessingMode argument is empty");

		mode = aMode;
		schedulerJob.getJobDataMap().put(LoaderJob.PROCESSING_MODE, mode);
	}

	@Override
	public void tearDown() {
		synchronized (lock) {
			try {
				if (scheduler.isShutdown())
					return;

				scheduler.shutdown(true);

				// waiting when all current job's finish it's work
				while (!scheduler.isShutdown())
					Thread.sleep(1L);
			} catch (SchedulerException e) {
				String message = "tearDown() fail";
				LOGGER.error(message, e);
				throw new RuntimeException(message, e);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
