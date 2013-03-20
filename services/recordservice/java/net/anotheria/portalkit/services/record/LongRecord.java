package net.anotheria.portalkit.services.record;

import java.io.Serializable;

/**
 * @author dagafonov
 */
public class LongRecord extends Record implements Serializable {

	private static final long serialVersionUID = 1L;

	private long value;

	public LongRecord(String aRecordId) {
		this(aRecordId, 0);
	}

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
