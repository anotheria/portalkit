package net.anotheria.portalkit.services.record;

/**
 * {@link Integer} implementation of a {@link Record}.
 * 
 * @author lrosenberg
 * @since 27.02.13 19:24
 */
public class IntRecord extends Record {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -3703098071575272870L;
	
	/**
	 * Value
	 */
	private int value;

	public IntRecord(String aRecordId) {
		this(aRecordId, 0);
	}

	public IntRecord(String aRecordId, int aValue) {
		super(aRecordId);
		value = aValue;
	}

	@Override
	public RecordType getType() {
		return RecordType.INT;
	}

	@Override
	public String getValueAsString() {
		return "" + getInt();
	}

	public int getInt() {
		return value;
	}
}
