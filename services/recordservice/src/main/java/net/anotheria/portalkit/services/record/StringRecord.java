package net.anotheria.portalkit.services.record;

/**
 * String implementation of a {@link Record}.
 * 
 * @author lrosenberg
 * @since 27.02.13 19:59
 */
public class StringRecord extends Record {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -6257708618031821828L;

	/**
	 * Value.
	 */
	private String value;

	/**
	 * Default constructor.
	 * 
	 * @param aRecordId	record id.
	 */
	public StringRecord(String aRecordId) {
		this(aRecordId, "");
	}

	/**
	 * Constructor with all fields.
	 * 
	 * @param aRecordId	record id.
	 * @param aValue	value.
	 */
	public StringRecord(String aRecordId, String aValue) {
		super(aRecordId);
		value = aValue;
	}

	@Override
	public RecordType getType() {
		return RecordType.STRING;
	}

	@Override
	public String getValueAsString() {
		return getString();
	}

	public String getString() {
		return value;
	}
}
