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

	/**
	 * Gets record collection.
	 * 
	 * @param ownerId owner id.
	 * @param collectionId collectio id.
	 * @return {@link RecordCollection}
	 * @throws RecordPersistenceServiceException if error.
	 */
	RecordCollection getCollection(String ownerId, String collectionId) throws RecordPersistenceServiceException;

	/**
	 * Updates record collection.
	 *
	 * @param ownerId owner id.
	 * @param collectionId collectio id.
	 * @param collection {@link RecordCollection}
	 * @throws RecordPersistenceServiceException if error.
	 */
	void updateCollection(String ownerId, String collectionId, RecordCollection collection) throws RecordPersistenceServiceException;

	/**
	 * Gets record by id.
	 *
	 * @param ownerId owner id.
	 * @param collectionId collectio id.
	 * @param recordId record id.
	 * @return {@link Record}
	 * @throws RecordPersistenceServiceException if error.
	 */
	Record getSingleRecord(String ownerId, String collectionId, String recordId) throws RecordPersistenceServiceException;

	/**
	 * Updates record.
	 *
	 * @param ownerId owner id.
	 * @param collectionId collectio id.
	 * @param record record.
	 * @throws RecordPersistenceServiceException if error.
	 */
	void updateSingleRecord(String ownerId, String collectionId, Record record) throws RecordPersistenceServiceException;

}
