package net.anotheria.portalkit.services.record;

import java.util.Collection;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceService;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

/**
 * {@link RecordService} default implementation.
 * 
 * @author lrosenberg
 * @since 27.02.13 09:13
 */
public class RecordServiceImpl implements RecordService {

	/**
	 * Default collection name.
	 */
	public static final String DEFAULT_COLLECTION_ID = "account";

	/**
	 * Persistence service instance.
	 */
	private RecordPersistenceService persistenceService;

	/**
	 * Cache of Record collections.
	 */
	private Cache<CacheKey, RecordCollection> recordCollectionCache;

	/**
	 * Lock manager.
	 */
	private IdBasedLockManager<CacheKey> idBasedLockManager = new SafeIdBasedLockManager<CacheKey>();

	/**
	 * Default constructor.
	 */
	public RecordServiceImpl() {
		recordCollectionCache = Caches.createHardwiredCache("record-collection");
		try {
			persistenceService = MetaFactory.get(RecordPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service.", e);
		}
	}

	private RecordCollection getRecordCollection(String ownerId, String collectionId) throws RecordServiceException {
		CacheKey ck = new CacheKey(ownerId, collectionId);
		IdBasedLock<CacheKey> lock = idBasedLockManager.obtainLock(ck);
		lock.lock();
		try {
			RecordCollection collection = recordCollectionCache.get(ck);
			if (collection == null) {
				RecordCollection newCollection = persistenceService.getCollection(ownerId, collectionId);
				recordCollectionCache.put(ck, newCollection);
				collection = newCollection;
			}
			return collection;
		} catch (RecordPersistenceServiceException ex) {
			throw new RecordServiceException("persistenceService.getCollection failed.", ex);
		} finally {
			lock.unlock();
		}
	}

	private void saveRecordCollection(String ownerId, RecordCollection collection) throws RecordServiceException {
		if (collection == null || collection.getCollectionId() == null) {
			throw new RecordServiceException("collectionId is null.");
		}
		CacheKey ck = new CacheKey(ownerId, collection.getCollectionId());
		IdBasedLock<CacheKey> lock = idBasedLockManager.obtainLock(ck);
		lock.lock();
		try {
			persistenceService.updateCollection(ownerId, collection.getCollectionId(), collection);
			recordCollectionCache.put(ck, collection);
		} catch (RecordPersistenceServiceException ex) {
			throw new RecordServiceException("persistenceService.updateCollection failed.", ex);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Record getRecord(String ownerId, String collectionId, String recordId) throws RecordServiceException {
		RecordCollection collection = getRecordCollection(ownerId, collectionId);
		return collection.getRecord(recordId);
	}

	@Override
	public Record getRecord(AccountId ownerId, String collectionId, String recordId) throws RecordServiceException {
		return getRecord(ownerId.getInternalId(), collectionId, recordId);
	}

	@Override
	public RecordSet getRecordSet(String ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException {
		RecordCollection collection = getRecordCollection(ownerId, collectionId);
		return collection.getRecordSet(recordIds);
	}

	@Override
	public RecordSet getRecordSet(AccountId ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException {
		return getRecordSet(ownerId.getInternalId(), collectionId, recordIds);
	}

	@Override
	public void setRecord(String ownerId, String collectionId, Record record) throws RecordServiceException {
		RecordCollection collection = getRecordCollection(ownerId, collectionId);
		collection.setRecord(record);
		saveRecordCollection(ownerId, collection);
	}

	@Override
	public void setRecord(AccountId ownerId, String collectionId, Record record) throws RecordServiceException {
		setRecord(ownerId.getInternalId(), collectionId, record);
	}

	@Override
	public void setRecords(AccountId ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException {
		setRecords(ownerId.getInternalId(), collectionId, recordSet);
	}

	@Override
	public void setRecords(String ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException {
		RecordCollection collection = getRecordCollection(ownerId, collectionId);
		collection.setRecords(recordSet.getRecords());
		saveRecordCollection(ownerId, collection);
	}

	@Override
	public Record getRecord(AccountId ownerId, String recordId) throws RecordServiceException {
		return getRecord(ownerId, DEFAULT_COLLECTION_ID, recordId);
	}

	@Override
	public RecordSet getRecordSet(AccountId ownerId, Collection<String> recordIds) throws RecordServiceException {
		return getRecordSet(ownerId, DEFAULT_COLLECTION_ID, recordIds);
	}

	@Override
	public void setRecord(AccountId ownerId, Record record) throws RecordServiceException {
		setRecord(ownerId, DEFAULT_COLLECTION_ID, record);
	}

	@Override
	public void setRecords(AccountId ownerId, RecordSet recordSet) throws RecordServiceException {
		setRecords(ownerId, DEFAULT_COLLECTION_ID, recordSet);
	}

	/**
	 * Cache key representation.
	 */
	private static class CacheKey {
		
		/**
		 * The Profilecollection owner id.
		 */
		private String ownerId;
		
		/**
		 * The id of the collection.
		 */
		private String collectionId;

		public CacheKey(String anOwnerId, String aCollectionId) {
			if (anOwnerId == null || anOwnerId.length() == 0)
				throw new IllegalArgumentException("OwnerId can't be null");
			if (aCollectionId == null || aCollectionId.length() == 0)
				throw new IllegalArgumentException("CollectionId can't be null");
			ownerId = anOwnerId;
			collectionId = aCollectionId;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof CacheKey && ownerId.equals(((CacheKey) o).ownerId) && collectionId.equals(((CacheKey) o).collectionId);
		}

		@Override
		public int hashCode() {
			int result = ownerId.hashCode();
			result = 31 * result + collectionId.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return ownerId + "#" + collectionId;
		}
	}
}
