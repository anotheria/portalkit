package net.anotheria.portalkit.services.record;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class contains the complete collection for one owner, meaning all elements in one collection of one user.
 * This is making it easier to load and cache collection-wise.
 *
 * @author lrosenberg
 * @since 04.01.13 15:21
 */
public class RecordCollection {
	/**
	 * Internal records storage.
	 */
	private ConcurrentMap<String, Record> records = new ConcurrentHashMap<String, Record>();
	/**
	 * The collection id of this profile collection.
	 */
	private String collectionId;

	RecordCollection(String aCollectionId){
		collectionId = aCollectionId;
	}

	public Record getRecord(String recordId){
		Record ret = records.get(recordId);
		if (ret==null)
			return Record.newEmptyRecord(recordId);
		return ret;
	}

	public void setRecord(Record record) {
		if (record.isEmpty())
			records.remove(record.getRecordId());
		else
			records.put(record.getRecordId(), record);
	}

	public void setRecords(Collection<Record> someRecords) {
		for (Record r : someRecords){
			if (r.isEmpty())
				records.remove(r.getRecordId());
			else
				records.put(r.getRecordId(), r);
		}

	}

	public String getCollectionId(){
		return collectionId;
	}

	@Override public String toString(){
		return collectionId+" elements: "+records;
	}

	public RecordSet getRecordSet(Collection<String> recordIds) {
		//TODO this method is actually not thread safe in a sense, that we can have a modification inbetween of our operation.
		//however, this is probably acceptable.
		RecordSet set = new RecordSet(recordIds.size());
		for (String rId : recordIds){
			Record r = records.get(rId);
			set.put(r == null ? Record.newEmptyRecord(rId) : r);
		}

		return set;
	}
}
