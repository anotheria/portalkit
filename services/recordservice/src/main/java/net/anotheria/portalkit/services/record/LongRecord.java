package net.anotheria.portalkit.services.record;

/**
 * {@link Long} implementation of a {@link Record}.
 * 
 * @author dagafonov
 */
public class LongRecord extends Record {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 102797783570463105L;

	/**
	 * Value.
	 */
	private long value;

	/**
	 * Default constructor.
	 * 
	 * @param aRecordId	record id.
	 */
	public LongRecord(String aRecordId) {
		this(aRecordId, 0);
	}

	/**
	 * Constructor with value.
	 * 
	 * @param aRecordId	record id.
	 * @param aValue	value.
	 */
	public LongRecord(String aRecordId, long aValue) {
		super(aRecordId);
		value = aValue;
	}

	@Override
	public RecordType getType() {
		return RecordType.LONG;
	}

	@Override
	public String getValueAsString() {
		return "" + getLong();
	}

	public long getLong() {
		return value;
	}
}
