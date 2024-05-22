package net.anotheria.portalkit.services.storage.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Serialization utility.
 * 
 * @author Alexandr Bolbat
 */
public final class SerializationUtils {

	/**
	 * Default constructor with preventing instantiations of this class.
	 */
	private SerializationUtils() {
		throw new IllegalAccessError();
	}

	/**
	 * Clone {@link Serializable} T {@link Collection}.
	 * 
	 * @param toClone
	 *            {@link Collection} with {@link Serializable} T to clone, can't be <code>null</code>
	 * @return {@link List} of T
	 */
	public static <T extends Serializable> List<T> clone(final Collection<T> toClone) {
		if (toClone == null)
			throw new IllegalArgumentException("toClone argument is null.");

		List<T> result = new ArrayList<T>();
		for (T obj : toClone)
			result.add(clone(obj));

		return result;
	}

	/**
	 * Clone {@link Serializable} T.<br>
	 * {@link org.apache.commons.lang3.SerializationUtils} used for cloning objects.
	 * 
	 * @param toClone
	 *            {@link Serializable} T to clone, can't be <code>null</code>
	 * @return a cloned T.
	 */
	public static <T extends Serializable> T clone(final T toClone) {
		if (toClone == null)
			throw new IllegalArgumentException("toClone argument is null.");

		@SuppressWarnings("unchecked")
		T clone = (T) org.apache.commons.lang3.SerializationUtils.clone(toClone);
		return clone;
	}

}
