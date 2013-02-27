package net.anotheria.portalkit.services.record;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

import java.util.Collection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.02.13 09:13
 */
public class RecordServiceImpl implements RecordService{

	public static final String DEFAULT_COLLECTION_ID = "account";

	private Cache<CacheKey, RecordCollection> recordCollectionCache;

	IdBasedLockManager<CacheKey> idBasedLockManager = new SafeIdBasedLockManager<CacheKey>();

	public RecordServiceImpl(){
		recordCollectionCache = Caches.createHardwiredCache("record-collection");
	}

	private RecordCollection getRecordCollection(String ownerId, String collectionId){
		CacheKey ck = new CacheKey(ownerId, collectionId);
		IdBasedLock lock = idBasedLockManager.obtainLock(ck);
		lock.lock();
		try{
			RecordCollection collection = recordCollectionCache.get(ck);

			if (collection==null){
				//TODO retrieve from service

				//TODO ok for now just to create in-cache-version
				RecordCollection newCollection = new RecordCollection();
				recordCollectionCache.put(ck, newCollection);
				collection = newCollection;
			}
			return collection;
		}finally{
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
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public RecordSet getRecordSet(AccountId ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setRecord(String ownerId, String collectionId, Record record) throws RecordServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setRecord(AccountId ownerId, String collectionId, Record record) throws RecordServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setRecords(AccountId ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setRecords(String ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
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
		setRecord(ownerId,  DEFAULT_COLLECTION_ID, record);
	}

	@Override
	public void setRecords(AccountId ownerId, RecordSet recordSet) throws RecordServiceException {
		setRecords(ownerId, DEFAULT_COLLECTION_ID, recordSet);
	}

	private static class CacheKey{
		/**
		 * The Profilecollection owner id.
		 */
		private String ownerId;
		/**
		 * The id of the collection.
		 */
		private String collectionId;

		public CacheKey(String anOwnerId, String aCollectionId){
			if (anOwnerId==null || anOwnerId.length()==0)
				throw new IllegalArgumentException("OwnerId can't be null");
			if (aCollectionId==null || aCollectionId.length()==0)
				throw new IllegalArgumentException("CollectionId can't be null");
			ownerId = anOwnerId;
			collectionId = aCollectionId;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof CacheKey && ownerId.equals(((CacheKey)o).ownerId) && collectionId.equals(((CacheKey)o).collectionId);
		}

		@Override
		public int hashCode() {
			int result = ownerId.hashCode();
			result = 31 * result + collectionId.hashCode();
			return result;
		}
	}
}
