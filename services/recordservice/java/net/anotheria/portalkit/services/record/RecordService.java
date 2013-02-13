package net.anotheria.portalkit.services.record;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;

/**
 * The record service.
 * The record service is used to store different type of data. Every method allows to work with accountId or ownerId;
 * using accoundId will implicitely associate the record collection with an account.
 *
 * @author lrosenberg
 * @since 11.12.12 17:37
 */
public interface RecordService {
	/**
	 * Returns a single record.
	 * @param ownerId
	 * @param collectionId
	 * @param recordId
	 * @return
	 * @throws RecordServiceException
	 */
	Record getRecord(String ownerId, String collectionId, String recordId) throws  RecordServiceException;

	/**
	 * Returns a single record associated with the account.
	 * @param ownerId
	 * @param collectionId
	 * @param recordId
	 * @return
	 * @throws RecordServiceException
	 */
	Record getRecord(AccountId ownerId, String collectionId, String recordId) throws  RecordServiceException;

	/**
	 * Returns a recordset specified by submitted record ids.
	 * @param ownerId
	 * @param collectionId
	 * @param recordIds
	 * @return
	 * @throws RecordServiceException
	 */
	RecordSet getRecordSet(String ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException;

	/**
	 * Returns a recordset specified by submitted record ids.
	 * @param ownerId
	 * @param collectionId
	 * @param recordIds
	 * @return
	 * @throws RecordServiceException
	 */
	RecordSet getRecordSet(AccountId ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException;

	void setRecord(String ownerId, String collectionId, Record record) throws RecordServiceException;

	void setRecord(AccountId ownerId, String collectionId, Record record) throws RecordServiceException;

	void setRecords(AccountId ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException;

	void setRecords(String ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException;
}
