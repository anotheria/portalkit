package net.anotheria.portalkit.services.record;

import java.io.Serializable;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.02.13 19:24
 */
public class IntRecord extends Record implements Serializable {

	private static final long serialVersionUID = 1L;

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
		return ""+getInt();
	}

	public int getInt(){
		return value;
	}
}
