package net.anotheria.portalkit.services.record;

import java.util.Collection;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import org.distributeme.annotation.DistributeMe;

/**
 * The record service. The record service is used to store different type of
 * data. Every method allows to work with accountId or ownerId; using accoundId
 * will implicitly associate the record collection with an account.
 * 
 * @author lrosenberg
 * @since 11.12.12 17:37
 */
@DistributeMe()
public interface RecordService extends Service {
	
	/**
	 * Returns a single record.
	 * 
	 * @param ownerId
	 * @param collectionId
	 * @param recordId
	 * @return
	 * @throws RecordServiceException
	 */
	Record getRecord(String ownerId, String collectionId, String recordId) throws RecordServiceException;

	/**
	 * Returns a single record associated with the account.
	 * 
	 * @param ownerId
	 * @param collectionId
	 * @param recordId
	 * @return
	 * @throws RecordServiceException
	 */
	Record getRecord(AccountId ownerId, String collectionId, String recordId) throws RecordServiceException;

	/**
	 * Returns a single record associated with the default collection and
	 * account.
	 * 
	 * @param ownerId
	 * @param recordId
	 * @return
	 * @throws RecordServiceException
	 */
	Record getRecord(AccountId ownerId, String recordId) throws RecordServiceException;

	/**
	 * Returns a recordset specified by submitted record ids.
	 * 
	 * @param ownerId
	 * @param collectionId
	 * @param recordIds
	 * @return
	 * @throws RecordServiceException
	 */
	RecordSet getRecordSet(String ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException;

	/**
	 * Returns a recordset specified by submitted record ids.
	 * 
	 * @param ownerId
	 * @param collectionId
	 * @param recordIds
	 * @return
	 * @throws RecordServiceException
	 */
	RecordSet getRecordSet(AccountId ownerId, String collectionId, Collection<String> recordIds) throws RecordServiceException;

	/**
	 * Returns a recordset specified by submitted record ids.
	 * 
	 * @param ownerId
	 * @param recordIds
	 * @return
	 * @throws RecordServiceException
	 */
	RecordSet getRecordSet(AccountId ownerId, Collection<String> recordIds) throws RecordServiceException;

	void setRecord(String ownerId, String collectionId, Record record) throws RecordServiceException;

	void setRecord(AccountId ownerId, String collectionId, Record record) throws RecordServiceException;

	void setRecords(AccountId ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException;

	void setRecords(String ownerId, String collectionId, RecordSet recordSet) throws RecordServiceException;

	void setRecord(AccountId ownerId, Record record) throws RecordServiceException;

	void setRecords(AccountId ownerId, RecordSet recordSet) throws RecordServiceException;
}
