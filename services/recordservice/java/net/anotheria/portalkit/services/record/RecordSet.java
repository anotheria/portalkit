package net.anotheria.portalkit.services.record;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Record set.
 * 
 * @author lrosenberg
 * @since 04.01.13 15:20
 */
public class RecordSet implements Serializable {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -3152621373008889484L;

	/**
	 * Map of records in this record set.
	 */
	private ConcurrentMap<String, Record> recordMap;

	/**
	 * Default constructor.
	 */
	public RecordSet() {
		this(50);
	}

	/**
	 * Constructor that extends default with this {code size}.
	 * 
	 * @param size
	 */
	public RecordSet(int size) {
		recordMap = new ConcurrentHashMap<String, Record>(size);
	}

	/**
	 * Gets record by id in this record set.
	 * 
	 * @param id
	 * @return {@link Record}
	 */
	public Record get(String id) {
		return recordMap.get(id);
	}

	/**
	 * Puts record into this record set.
	 * 
	 * @param record
	 */
	public void put(Record record) {
		recordMap.put(record.getRecordId(), record);
	}

	@Override
	public String toString() {
		return recordMap.toString();
	}

	public Collection<Record> getRecords() {
		return recordMap.values();
	}
}
