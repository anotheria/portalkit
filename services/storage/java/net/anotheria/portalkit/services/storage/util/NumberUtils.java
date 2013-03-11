package net.anotheria.portalkit.services.storage.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility with some helper functionality related to numbers.
 * 
 * @author Alexandr Bolbat
 */
public final class NumberUtils {

	/**
	 * Compare two instances of {@link Number} between each other.
	 * 
	 * @param first
	 *            first {@link Number}, can't be <code>null</code>
	 * @param second
	 *            second {@link Number}, can't be <code>null</code>
	 * @return -1, 0, or 1 as first {@link Number} is less than, equal to, or greater than second {@link Number}
	 */
	public static int compare(final Number first, final Number second) {
		if (first == null)
			throw new IllegalArgumentException("first argument is null.");
		if (second == null)
			throw new IllegalArgumentException("second argument is null.");

		// if first and second numbers not the same type we use BigDecimal type to compare it's
		if (!first.getClass().equals(second.getClass()))
			return new BigDecimal(first.doubleValue()).compareTo(new BigDecimal(second.doubleValue()));

		// if both numbers are the same type
		if (first instanceof Byte && second instanceof Byte) { // byte
			return ((Byte) first).compareTo((Byte) second);
		} else if (first instanceof Short && second instanceof Short) { // short
			return ((Short) first).compareTo((Short) second);
		} else if (first instanceof Integer && second instanceof Integer) { // integer
			return ((Integer) first).compareTo((Integer) second);
		} else if (first instanceof AtomicInteger && second instanceof AtomicInteger) { // atomic integer
			return ((Integer) first.intValue()).compareTo(second.intValue());
		} else if (first instanceof BigInteger && second instanceof BigInteger) { // big integer
			return ((BigInteger) first).compareTo((BigInteger) second);
		} else if (first instanceof Long && second instanceof Long) { // long
			return ((Long) first).compareTo((Long) second);
		} else if (first instanceof AtomicLong && second instanceof AtomicLong) { // atomic long
			return ((Long) first.longValue()).compareTo(second.longValue());
		} else if (first instanceof Float && second instanceof Float) { // float
			return org.apache.commons.lang.math.NumberUtils.compare(first.floatValue(), second.floatValue());
		} else if (first instanceof Double && second instanceof Double) { // double
			return org.apache.commons.lang.math.NumberUtils.compare(first.doubleValue(), second.doubleValue());
		} else if (first instanceof BigDecimal && second instanceof BigDecimal) { // big decimal
			return ((BigDecimal) first).compareTo((BigDecimal) second);
		} else {
			// if other sub-types of Number we use BigDecimal type to compare it's
			return new BigDecimal(first.doubleValue()).compareTo(new BigDecimal(second.doubleValue()));
		}
	}
}
