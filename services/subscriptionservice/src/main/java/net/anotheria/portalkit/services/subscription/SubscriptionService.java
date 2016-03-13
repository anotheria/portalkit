package net.anotheria.portalkit.services.subscription;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.03.16 15:50
 */
public interface SubscriptionService extends Service {
	/**
	 * Returns active subscription of the account or null if there is no active subscription.
	 * @param accountId
	 * @return
	 * @throws SubscriptionServiceException
	 */
	Subscription getActiveSubscriptionForAccount(AccountId accountId) throws SubscriptionServiceException;


	///// This part of the interface handles transaction log entries.

	/**
	 * Transaction log
	 * @param toAdd
	 * @throws SubscriptionServiceException
	 */
	void addTransactionLogEntry(TransactionLogEntry toAdd) throws SubscriptionServiceException;

	/**
	 * Returns all transaction log entries for a user.
	 * @param owner
	 * @return
	 * @throws SubscriptionServiceException
	 */
	List<TransactionLogEntry> getTransactionLogEntries(AccountId owner) throws SubscriptionServiceException;


}
