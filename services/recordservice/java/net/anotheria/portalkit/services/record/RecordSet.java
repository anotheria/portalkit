package net.anotheria.portalkit.services.record;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 04.01.13 15:20
 */
public class RecordSet {

	private ConcurrentMap<String, Record> recordMap;

	public RecordSet(){
		this(50);
	}

	public RecordSet(int size) {
		recordMap = new ConcurrentHashMap<String, Record>(size);
	}

	public Record get(String id) {
		return recordMap.get(id);
	}

	public void put(Record record){
		recordMap.put(record.getRecordId(), record);
	}

	@Override public String toString(){
		return recordMap.toString();
	}

	public Collection<Record> getRecords(){
		return recordMap.values();
	}
}
