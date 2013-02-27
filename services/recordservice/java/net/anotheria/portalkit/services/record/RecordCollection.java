package net.anotheria.portalkit.services.record;

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
	private ConcurrentMap<String, Record> records = new ConcurrentHashMap<String, Record>();

	RecordCollection(){

	}

	public Record getRecord(String recordId){
		Record ret = records.get(recordId);
		if (ret==null)
			return Record.newEmptyRecord(recordId);
		return ret;
	}
}
