package net.anotheria.portalkit.services.accountlist.sorter;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container class where will be registered comparators for field sorting.
 * 
 * @author dagafonov
 * 
 * @param <T>
 */
public class ComparatorContainer<T> {

	/**
	 * Storage of all comparators.
	 */
	private final ConcurrentHashMap<String, Comparator<T>> comparators = new ConcurrentHashMap<String, Comparator<T>>();

	/**
	 * Adds comparator to the storage.
	 * 
	 * @param fieldName
	 * @param comparator
	 */
	public void addComparator(String fieldName, Comparator<T> comparator) {
		comparators.putIfAbsent(fieldName, comparator);
	}

	/**
	 * 
	 * @param fieldName
	 * @param comparator
	 */
	public void addComparator(FieldComparator<T> fieldComparator) {
		comparators.putIfAbsent(fieldComparator.getFieldName(), fieldComparator.getComparator());
	}

	/**
	 * Default string comparator.
	 */
	private final Comparator<T> stringComparator = new Comparator<T>() {
		@Override
		public int compare(T o1, T o2) {
			return String.valueOf(o1).compareTo(String.valueOf(o2));
		}
	};

	/**
	 * Returns {@link Comparator<T>}. If this comparator does not exist in
	 * storage - return default string comparator.
	 * 
	 * @param fieldName
	 * @return {@link Comparator<T>}
	 */
	public Comparator<T> getComparator(String fieldName) {
		Comparator<T> comp = comparators.get(fieldName);
		if (comp == null) {
			return stringComparator;
		}
		return comp;
	}

	/**
	 * Returns {@link Comparator<T>}. If this comparator does not exist in
	 * storage - return default string comparator.
	 * 
	 * @param sortBy
	 * @return {@link Comparator<T>}
	 */
	public Comparator<T> getComparator(FieldComparator<T> sortBy) {
		if (sortBy == null) {
			return stringComparator;
		}
		return getComparator(sortBy.getFieldName());
	}
}
