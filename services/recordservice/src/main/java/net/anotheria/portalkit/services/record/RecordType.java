package net.anotheria.portalkit.services.record;

/**
 * Record types enumeration.
 * 
 * @author lrosenberg
 * @since 27.02.13 10:10
 */
public enum RecordType {

	/**
	 * 
	 */
	NONE,

	/**
	 * 
	 */
	INT,

	/**
	 * 
	 */
	LONG,

	/**
	 * 
	 */
	STRING;

	/**
	 * Searches {@link RecordType} by order.
	 * 
	 * @param ordinal	order.
	 * @return {@link RecordType}
	 */
	public static RecordType find(int ordinal) {
		for (RecordType v : values()) {
			if (v.ordinal() == ordinal) {
				return v;
			}
		}
		return NONE;
	}
}
