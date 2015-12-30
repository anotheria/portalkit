package net.anotheria.portalkit.services.common.scheduledqueue;

import java.util.List;

/**
 * Custom loader interface. Scheduler invoke loader by it's schedule and get required elements from it.
 * 
 * @author Alexandr Bolbat
 */
public interface Loader {

	/**
	 * Load elements to the queue.
	 * 
	 * @return {@link List} of {@link Object}
	 * @throws LoadingException
	 */
	List<Object> load() throws LoadingException;

}
