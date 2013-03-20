package net.anotheria.portalkit.services.record.persistence.inmemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.portalkit.services.record.Record;
import net.anotheria.portalkit.services.record.RecordCollection;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceService;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceServiceException;

/**
 * 
 * @author dagafonov
 * 
 */
public class InMemoryRecordPersistenceServiceImpl implements RecordPersistenceService {

	private Map<OwnerCollectionKey, RecordCollection> storage = new ConcurrentHashMap<OwnerCollectionKey, RecordCollection>();

	@Override
	public RecordCollection getCollection(String ownerId, String collectionId) throws RecordPersistenceServiceException {
		RecordCollection collection = storage.get(new OwnerCollectionKey(ownerId, collectionId));
		if (collection == null) {
			collection = new RecordCollection(collectionId);
			storage.put(new OwnerCollectionKey(ownerId, collectionId), collection);
		}
		return collection;
	}

	@Override
	public void updateCollection(String ownerId, String collectionId, RecordCollection collection) throws RecordPersistenceServiceException {
		if (!collectionId.equals(collection.getCollectionId())) {
			throw new RecordPersistenceServiceException("collectionId and collection.getCollectionId() are not equal.");
		}
		storage.put(new OwnerCollectionKey(ownerId, collection.getCollectionId()), collection);
	}

	@Override
	public Record getSingleRecord(String ownerId, String collectionId, String recordId) throws RecordPersistenceServiceException {
		RecordCollection collection = getCollection(ownerId, collectionId);
		return collection.getRecord(recordId);
	}

	@Override
	public void updateSingleRecord(String ownerId, String collectionId, Record record) throws RecordPersistenceServiceException {
		RecordCollection collection = getCollection(ownerId, collectionId);
		collection.setRecord(record);
	}

	private class OwnerCollectionKey {

		private String ownerId;
		private String collectionId;

		public OwnerCollectionKey(String ownerId, String collectionId) {
			this.ownerId = ownerId;
			this.collectionId = collectionId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((collectionId == null) ? 0 : collectionId.hashCode());
			result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OwnerCollectionKey other = (OwnerCollectionKey) obj;
			if (collectionId == null) {
				if (other.collectionId != null)
					return false;
			} else if (!collectionId.equals(other.collectionId))
				return false;
			if (ownerId == null) {
				if (other.ownerId != null)
					return false;
			} else if (!ownerId.equals(other.ownerId))
				return false;
			return true;
		}

	}

}
