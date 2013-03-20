package net.anotheria.portalkit.services.record.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.record.Record;
import net.anotheria.portalkit.services.record.RecordCollection;

/**
 * Record persistence service interface.
 *
 * @author lrosenberg
 * @since 04.01.13 15:21
 */
public interface RecordPersistenceService extends Service {
	
	RecordCollection getCollection(String ownerId, String collectionId) throws RecordPersistenceServiceException;

	void updateCollection(String ownerId, String collectionId, RecordCollection collection) throws RecordPersistenceServiceException;

	Record getSingleRecord(String ownerId, String collectionId, String recordId) throws RecordPersistenceServiceException;

	void updateSingleRecord(String ownerId, String collectionId, Record record) throws RecordPersistenceServiceException;

}
