package net.anotheria.portalkit.services.subscription.persistence.transaction;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 16:41
 */
@Monitor
@Service
@Transactional
public class TransactionLogEntryPersistenceServiceImpl implements TransactionLogEntryPersistenceService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void addTransactionLogEntry(TransactionLogEntryEntity toAdd) throws TransactionPersistenceException {
		entityManager.persist(toAdd);
	}

	@Override
	public void deleteTransactionLogEntrys(String accountId) throws TransactionPersistenceException {

		Query query = entityManager.createNamedQuery(TransactionLogEntryEntity.JPQL_DELETE_TRANSACTION_LOG)
				.setParameter("accountId", accountId);

		query.executeUpdate();
	}

	@Override
	public List<TransactionLogEntryEntity> getTransactionLogEntries(String ownerId) throws TransactionPersistenceException {
		TypedQuery<TransactionLogEntryEntity> q = entityManager.createNamedQuery(TransactionLogEntryEntity.JPQL_GET_BY_ACCOUNT_ID, TransactionLogEntryEntity.class);
		q.setParameter("accountId", ownerId);
		return q.getResultList();

	}

	@Override
	public List<TransactionLogEntryEntity> getTransactionLogEntries() throws TransactionPersistenceException {
		TypedQuery<TransactionLogEntryEntity> q = entityManager.createNamedQuery(TransactionLogEntryEntity.JPQL_GET_ALL, TransactionLogEntryEntity.class);
		return q.getResultList();
	}
}
