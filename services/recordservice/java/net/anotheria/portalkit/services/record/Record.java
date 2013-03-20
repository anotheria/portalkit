package net.anotheria.portalkit.services.record;

import java.io.Serializable;

/**
 * Represents a single record in a record service. The record is the smallest
 * possible unit in the record service.
 * 
 * @author lrosenberg
 * @since 03.01.13 18:07
 */
public abstract class Record implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Id of the record.
	 */
	private String recordId;

	protected Record(String aRecordId) {
		recordId = aRecordId;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Record && recordId.equals(((Record) o).recordId);
	}

	@Override
	public int hashCode() {
		return recordId.hashCode();
	}

	public String getRecordId() {
		return recordId;
	}

	public abstract RecordType getType();

	public static Record newEmptyRecord(String recordId) {
		return new EmptyRecord(recordId);
	}

	public boolean isEmpty() {
		return false;
	}

	public abstract String getValueAsString();

	@Override
	public String toString() {
		return "(" + getRecordId() + ", " + getType() + ", " + getValueAsString() + ")";
	}
}
