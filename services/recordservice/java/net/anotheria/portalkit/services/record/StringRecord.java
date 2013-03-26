package net.anotheria.portalkit.services.record;


/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.02.13 19:59
 */
public class StringRecord extends Record {

	private static final long serialVersionUID = 1L;

	private String value;

	public StringRecord(String aRecordId) {
		this(aRecordId, "");
	}

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

	public String getString(){
		return value;
	}
}
