package net.anotheria.portalkit.services.storage.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for slicing original collection by given parameters.
 * 
 * @author Alexandr Bolbat
 */
public final class Slicer {

	/**
	 * Default constructor with preventing instantiations of this class.
	 */
	private Slicer() {
		throw new IllegalAccessError();
	}

	/**
	 * Return elements slice from original list. If original list are <code>null</code> or empty - empty {@link ArrayList} will be returned.
	 * 
	 * @param list
	 *            original list
	 * @param offset
	 *            offset, minimum offset is '0', if offset less than '0' default will be used
	 * @param amount
	 *            result elements size
	 * @return {@link List} of <T>
	 */
	public static <T> List<T> slice(final List<T> list, final int offset, final int amount) {
		if (list == null || list.isEmpty())
			return new ArrayList<T>();

		int aSize = list.size();
		int aOffset = offset < 0 ? 0 : offset;
		int aAmount = amount < 0 ? 0 : amount;

		if (aAmount == 0 || aOffset > aSize - 1)
			return new ArrayList<T>();

		int endIndex = Math.min(aSize, aOffset + aAmount);
		return new ArrayList<T>(list.subList(aOffset, endIndex));
	}

	/**
	 * Return elements slice from original list. If original list are <code>null</code> or empty - empty {@link ArrayList} will be returned.
	 * 
	 * @param list
	 *            original list
	 * @param page
	 *            page number, minimum page is '0', if page less than '0' default will be used
	 * @param elements
	 *            element on page
	 * @return {@link List} of <T>
	 */
	public static <T> List<T> sliceTo(final List<T> list, final int page, final int elements) {
		if (elements < 1)
			return new ArrayList<T>();

		int startIndex = 0;
		if (page == 1)
			startIndex = elements;
		if (page > 1)
			startIndex = elements * (page + 1) - 1;

		return slice(list, startIndex, elements);
	}

}
