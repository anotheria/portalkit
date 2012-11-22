package net.anotheria.portalkit.services.common.scheduledqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
	private static final AtomicInteger loaded = new AtomicInteger(0);

	/**
	 * Synchronization lock.
	 */
	private static final Object LOCK = new Object();

	@Override
	public List<Object> load() throws LoadingException {
		List<Object> result = new ArrayList<Object>();

		if (loaded.get() < MAX_TO_LOAD) {
			synchronized (LOCK) {
				if (loaded.get() < MAX_TO_LOAD) {
					result.add(System.currentTimeMillis() + "_obj_" + loaded);
					loaded.incrementAndGet();
				}
			}
		}

		return result;
	}

	public int getLoaded() {
		return loaded.get();
	}

}
