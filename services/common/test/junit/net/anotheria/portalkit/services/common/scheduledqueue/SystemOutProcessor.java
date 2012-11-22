package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * {@link Processor} testing implementation.
 * 
 * @author Alexandr Bolbat
 */
public class SystemOutProcessor implements Processor {

	/**
	 * Processed elements amount.
	 */
	private int processed;

	@Override
	public void process(final Object element) throws ProcessingException {
		System.out.println(System.currentTimeMillis() + " element: " + element);
		processed++;
	}

	public int getProcessed() {
		return processed;
	}

}
