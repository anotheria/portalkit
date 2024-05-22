package net.anotheria.portalkit.services.subscription;

import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.persistence.subscription.SubscriptionDO;
import net.anotheria.portalkit.services.subscription.persistence.subscription.SubscriptionPersistenceException;
import net.anotheria.portalkit.services.subscription.persistence.subscription.SubscriptionPersistenceService;
import net.anotheria.portalkit.services.subscription.persistence.subscription.TransactionDO;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryEntity;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryPersistenceService;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 16:16
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService, EntityManagingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

	@Autowired
	private TransactionLogEntryPersistenceService transactionLogEntryPersistenceService;

	@Autowired
	private SubscriptionPersistenceService subscriptionPersistenceService;

	public SubscriptionServiceImpl() {
		EntityManagingServices.createEntityCounter(this, "Transactions", "Subscriptions", "Cancellations");
	}

	@Override
	public int getEntityCount(String s) {
		try {
			switch (s) {
				case "Transactions":
					return Long.valueOf(subscriptionPersistenceService.getTransactionsCount()).intValue();
				case "Subscriptions":
					return Long.valueOf(subscriptionPersistenceService.getSubscriptionsCount()).intValue();
				case "Cancellations":
					return Long.valueOf(subscriptionPersistenceService.getCancellationsCount()).intValue();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return 0;
	}

	@Override
	public Subscription getActiveSubscriptionForAccount(AccountId accountId) throws SubscriptionServiceException {

		SubscriptionDO subscription = null;

		try {
			subscription = subscriptionPersistenceService.getActiveSubscriptionForAccount(accountId.getInternalId());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException(e.getMessage(), e);
		}

		return new Subscription(subscription);
	}

	@Override
	public Subscription getSubscriptionForAccount(AccountId accountId) throws SubscriptionServiceException {

		SubscriptionDO subscription = null;

		try {
			subscription = subscriptionPersistenceService.getSubscriptionForAccount(accountId.getInternalId());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException(e.getMessage(), e);
		}

		return new Subscription(subscription);
	}

	@Override
	public List<Subscription> getSubscriptions() throws SubscriptionServiceException {

		List<Subscription> subscriptions = new ArrayList<Subscription>();
		List<SubscriptionDO> subscriptionDOs = null;

		try {
			subscriptionDOs = subscriptionPersistenceService.getSubscriptions();
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while getting subscriptions", e);
		}

		for(SubscriptionDO subscriptionDO : subscriptionDOs) {
			subscriptions.add(new Subscription(subscriptionDO));
		}

		return subscriptions;
	}

	@Override
	public void deleteSubscriptions(AccountId accountId) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.deleteSubscriptions(accountId.getInternalId());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while removing subscriptions", e);
		}
	}

	@Override
	public void deleteTransactions(AccountId accountId) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.deleteTransactions(accountId.getInternalId());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while removing transactions", e);
		}
	}

	@Override
	public List<Transaction> getTransactions() throws SubscriptionServiceException {

		List<TransactionDO> transactionDOs = null;

		try {
			transactionDOs = subscriptionPersistenceService.getTransactions();
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while getting transactions", e);
		}

		List<Transaction> transactions = new ArrayList<Transaction>();

		for(TransactionDO transactionDO : transactionDOs) {
			transactions.add(new Transaction(transactionDO));
		}

		return transactions;
	}

	@Override
	public void addSubscription(Subscription subscription) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.saveSubscription(subscription.toDO());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while adding new subscription", e);
		}
	}

	@Override
	public void updateSubscription(Subscription subscription) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.updateSubscription(subscription.toDO());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while updating subscription", e);
		}
	}

	@Override
	public void addTransaction(Transaction transaction) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.saveTransaction(transaction.toDO());
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while adding new transaction", e);
		}
	}

	@Override
	public void addTransactionLogEntry(TransactionLogEntry toAdd) throws SubscriptionServiceException {
		try {
			transactionLogEntryPersistenceService.addTransactionLogEntry(new TransactionLogEntryEntity(toAdd));
		} catch (TransactionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while adding new transaction log", e);
		}
	}

	@Override
	public List<TransactionLogEntry> getTransactionLogEntries(AccountId owner) throws SubscriptionServiceException {

		List<TransactionLogEntryEntity> fromService = null;

		try {
			fromService = transactionLogEntryPersistenceService.getTransactionLogEntries(owner.getInternalId());
		} catch (TransactionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while getting transaction log entries", e);
		}

		return convertToTransactionLogEntry(fromService);
	}

	@Override
	public List<TransactionLogEntry> getTransactionLogEntries() throws SubscriptionServiceException {

		List<TransactionLogEntryEntity> fromService = null;

		try {
			fromService = transactionLogEntryPersistenceService.getTransactionLogEntries();
		} catch (TransactionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while getting transaction log entries", e);
		}

		return convertToTransactionLogEntry(fromService);
	}

	@Override
	public List<TransactionLogEntry> getTransactionLogEntriesByMessageMask(String messageMask) throws SubscriptionServiceException {
		List<TransactionLogEntryEntity> fromService = null;

		try {
			fromService = transactionLogEntryPersistenceService.getTransactionLogEntriesByMessageMask(messageMask);
		} catch (TransactionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while getting transaction log entries", e);
		}

		return convertToTransactionLogEntry(fromService);
	}

	@Override
	public void deleteTransactionLogs(AccountId accountId) throws SubscriptionServiceException {
		try {
			transactionLogEntryPersistenceService.deleteTransactionLogEntrys(accountId.getInternalId());
		} catch (TransactionPersistenceException e) {
			throw new SubscriptionServiceException("Error occurred while removing transaction log entries", e);
		}
	}

	@Override
	public void saveCancellation(Cancellation cancellation) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.saveCancellation(cancellation);
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Unable to save cancellation", e);
		}
	}

	@Override
	public void deleteCancellation(String accountId) throws SubscriptionServiceException {
		try {
			subscriptionPersistenceService.deleteCancellation(accountId);
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Unable to delete cancellation", e);
		}
	}

	@Override
	public Cancellation getCancellationById(String accountId) throws SubscriptionServiceException {
		try {
			return subscriptionPersistenceService.getCancellationById(accountId);
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Unable to get cancellation by id", e);
		}
	}

	@Override
	public List<Cancellation> getCancellations() throws SubscriptionServiceException {
		try {
			return subscriptionPersistenceService.getCancellations();
		} catch (SubscriptionPersistenceException e) {
			throw new SubscriptionServiceException("Unable to get all cancellations", e);
		}
	}

	private List<TransactionLogEntry> convertToTransactionLogEntry(List<TransactionLogEntryEntity> fromService) {

		if (fromService == null || fromService.size() == 0)
			return Collections.emptyList();

		List<TransactionLogEntry> ret = new ArrayList<TransactionLogEntry>();

		for (TransactionLogEntryEntity ee : fromService){
			ret.add(ee.toBusinessObject());
		}

		return ret;
	}
}
