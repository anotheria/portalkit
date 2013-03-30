package net.anotheria.portalkit.services.accountlist.sorter;

import java.util.Comparator;

/**
 * General interface for all list of field comparators.
 * 
 * @author dagafonov
 * 
 * @param <T>
 */
public interface FieldComparator<T> {

	/**
	 * 
	 * @return {@link String}
	 */
	String getFieldName();

	/**
	 * 
	 * @return {@link Comparator<T>}
	 */
	Comparator<T> getComparator();
}