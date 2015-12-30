package net.anotheria.portalkit.services.common.scheduledqueue;

/**
 * Custom processor interface. Processor invoking for each queued element.
 * 
 * @author Alexandr Bolbat
 */
public interface Processor {

	/**
	 * Process queued element.
	 * 
	 * @param element
	 *            - queued element
	 * @throws ProcessingException
	 */
	void process(Object element) throws ProcessingException;

}
