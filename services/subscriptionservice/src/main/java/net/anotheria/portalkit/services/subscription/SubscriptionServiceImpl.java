package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryEntity;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 16:16
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

	@Autowired
	private TransactionLogEntryPersistenceService transactionLogEntryPersistenceService;

	@Override
	public Subscription getActiveSubscriptionForAccount(AccountId accountId) throws SubscriptionServiceException {
		return null;
	}

	@Override
	public void addTransactionLogEntry(TransactionLogEntry toAdd) throws SubscriptionServiceException {
		transactionLogEntryPersistenceService.addTransactionLogEntry(new TransactionLogEntryEntity(toAdd));
	}

	@Override
	public List<TransactionLogEntry> getTransactionLogEntries(AccountId owner) throws SubscriptionServiceException {
		List<TransactionLogEntryEntity> fromService = transactionLogEntryPersistenceService.getTransactionLogEntries(owner.getInternalId());
		if (fromService == null || fromService.size() == 0)
			return Collections.emptyList();
		LinkedList<TransactionLogEntry> ret = new LinkedList<>();
		for (TransactionLogEntryEntity ee : fromService){
			ret.add(ee.toBusinessObject());
		}
		return ret;
	}
}
