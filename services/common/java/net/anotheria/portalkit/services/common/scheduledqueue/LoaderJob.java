package net.anotheria.portalkit.services.common.scheduledqueue;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Quartz job implementation for {@link Loader} execution.
 * 
 * @author Alexandr Bolbat
 */
public final class LoaderJob implements Job {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(LoaderJob.class);

	/**
	 * Configured {@link Processor}.
	 */
	public static final String LOADER = "loader";

	/**
	 * Configured {@link Processor}.
	 */
	public static final String PROCESSOR = "processor";

	/**
	 * Configured {@link ProcessingMode}.
	 */
	public static final String PROCESSING_MODE = "processing_mode";

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		Object loaderObj = context.getJobDetail().getJobDataMap().get(LOADER);
		if (!(loaderObj instanceof Loader)) {
			LOGGER.error("execute(context) fail. No configured Loader.");
			return;
		}
		Loader loader = Loader.class.cast(loaderObj);

		Object processorObj = context.getJobDetail().getJobDataMap().get(PROCESSOR);
		if (!(processorObj instanceof Processor)) {
			LOGGER.error("execute(context) fail. No configured Processor.");
			return;
		}
		Processor processor = Processor.class.cast(processorObj);

		ProcessingMode processingMode = ProcessingMode.DEFAULT;
		Object processingModeObj = context.getJobDetail().getJobDataMap().get(PROCESSING_MODE);
		if (!(processingModeObj instanceof ProcessingMode)) {
			LOGGER.warn("execute(context) fail. No configured ProcessingMode.");
			return;
		}
		processingMode = ProcessingMode.class.cast(processingModeObj);

		try {
			debug("executing " + LoaderJob.class);
			List<Object> result = loader.load();
			debug("loaded " + (result != null ? result.size() : 0) + " elements");

			if (result == null || result.isEmpty())
				return;

			if (processingMode == ProcessingMode.SYNC) {
				for (Object element : result) {
					try {
						debug("processing element[" + element + "]");
						processor.process(element);
					} catch (ProcessingException e) {
						LOGGER.error("execute(context) processing fail. Skipping element[" + element + "].", e);
					} catch (Exception e) {
						LOGGER.error("execute(context) processing fail. Skipping element[" + element + "].", e);
					}
				}

				return;
			}

			// TODO: put result to queue to process in ASYNC mode
		} catch (LoadingException e) {
			String message = "execute(context) loading fail.";
			LOGGER.error(message, e);
		} catch (Exception e) {
			String message = "execute(context) fail";
			LOGGER.error(message, e);
			throw new JobExecutionException(message, e);
		}
	}

	/**
	 * Writing debug message to log if debug is enabled.
	 * 
	 * @param message
	 *            - message to write
	 */
	private static void debug(final String message) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(message);
	}

}
