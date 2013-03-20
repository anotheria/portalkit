package net.anotheria.portalkit.services.record;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.02.13 10:10
 */
public enum RecordType {
	NONE,
	INT,
	LONG,
	STRING;
	
	public static RecordType find(int ordinal) {
		for (RecordType v : values()) {
			if (v.ordinal() == ordinal) {
				return v;
			}
		}
		return NONE;
	}
}
