package net.anotheria.portalkit.services.subscription.persistence.transaction;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 16:33
 */
@Service
public interface TransactionLogEntryPersistenceService {
	/**
	 * Transaction log
	 * @param toAdd
	 */
	void addTransactionLogEntry(TransactionLogEntryEntity toAdd) throws TransactionPersistenceException;

	/**
	 * Transaction log
	 * @param accountId
	 */
	void deleteTransactionLogEntrys(String accountId) throws TransactionPersistenceException;

	/**
	 * Returns all transaction log entries for a user.
	 * @param ownerId
	 * @return
	 */
	List<TransactionLogEntryEntity> getTransactionLogEntries(String ownerId) throws TransactionPersistenceException;

	/**
	 * Returns all transaction log entries.
	 * @return
	 */
	List<TransactionLogEntryEntity> getTransactionLogEntries() throws TransactionPersistenceException;

	/**
	 * Returns all transaction log entries for given message mask
	 *
	 * @param messageMask message mask
	 * @return			  list of transaction log entries
	 * @throws TransactionPersistenceException if any errors occurs
	 */
	List<TransactionLogEntryEntity> getTransactionLogEntriesByMessageMask(String messageMask) throws TransactionPersistenceException;

	long getTransactionLogEntriesCount() throws TransactionPersistenceException;

}
