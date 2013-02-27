package net.anotheria.portalkit.services.record;

/**
 * Represents a single record in a record service. The record is the smallest possible unity in the record service.
 *
 * @author lrosenberg
 * @since 03.01.13 18:07
 */
public abstract class Record  {

	private String recordId;
	private RecordType type;

	protected Record(String aRecordId){
		recordId = aRecordId;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Record && recordId.equals(((Record)o).recordId);
	}

	@Override
	public int hashCode() {
		return recordId.hashCode();
	}

	public String getRecordId(){
		return recordId;
	}

	public RecordType getType(){
		return type;
	}

	public static Record newEmptyRecord(String recordId){
		return new EmptyRecord(recordId);
	}

	public boolean isEmpty(){
		return false;
	}
}
