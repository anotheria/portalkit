package net.anotheria.portalkit.services.common.scheduledqueue;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Loader} testing implementation.
 * 
 * @author Alexandr Bolbat
 */
public class RandomGenerationLoader implements Loader {

	/**
	 * Maximum elements to load.
	 */
	public static final int MAX_TO_LOAD = 100;

	/**
	 * Loaded elements amount.
	 */
	private int loaded;

	@Override
	public synchronized List<Object> load() throws LoadingException {
		List<Object> result = new ArrayList<Object>();

		if (loaded < MAX_TO_LOAD) {
			result.add(System.currentTimeMillis() + "_obj_" + loaded);
			loaded++;
		}

		return result;
	}

	public int getLoaded() {
		return loaded;
	}

}
