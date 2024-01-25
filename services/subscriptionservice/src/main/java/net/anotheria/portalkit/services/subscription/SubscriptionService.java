package net.anotheria.portalkit.services.subscription;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.03.16 15:50
 */
@DistributeMe(
		initcode = {
				"net.anotheria.portalkit.services.subscription.SubscriptionServiceSpringConfigurator.configure();"
		}
)
@FailBy(strategyClass=RetryCallOnce.class)
public interface SubscriptionService extends Service {
	/**
	 * Returns active subscription of the account or null if there is no active subscription.
	 * @param accountId
	 * @return
	 * @throws SubscriptionServiceException
	 */
	Subscription getActiveSubscriptionForAccount(AccountId accountId) throws SubscriptionServiceException;

	/**
	 * Returns subscription of the account or null if there is no subscription.
	 * @param accountId
	 * @return
	 * @throws SubscriptionServiceException
	 */
	Subscription getSubscriptionForAccount(AccountId accountId) throws SubscriptionServiceException;

	/**
	 * Get all subscriptions.
	 *
	 * @throws SubscriptionServiceException
	 * */
	List<Subscription> getSubscriptions() throws SubscriptionServiceException;

	/**
	 * Delete all subscriptions.
	 *
	 * @throws SubscriptionServiceException
	 * */
	void deleteSubscriptions(AccountId accountId) throws SubscriptionServiceException;

	/**
	 * Delete all transactions.
	 *
	 * @throws SubscriptionServiceException
	 * */
	void deleteTransactions(AccountId accountId) throws SubscriptionServiceException;

	/**
	 * Get all transactions.
	 *
	 * @throws SubscriptionServiceException
	 * */
	List<Transaction> getTransactions() throws SubscriptionServiceException;

	/**
	 * Adds new subscription.
	 *
	 * @param subscription	user subscription.
	 * @throws SubscriptionServiceException
	 * */
	void addSubscription(Subscription subscription) throws SubscriptionServiceException;

	/**
	 * Update existing subscription.
	 *
	 * @param subscription	user subscription.
	 * @throws SubscriptionServiceException
	 * */
	void updateSubscription(Subscription subscription) throws SubscriptionServiceException;

	/**
	 * Adds new transaction.
	 *
	 * @param transaction	payment transaction.
	 * @throws SubscriptionServiceException
	 * */
	void addTransaction(Transaction transaction) throws SubscriptionServiceException;

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
	 * @return list of {@link TransactionLogEntry}
	 * @throws SubscriptionServiceException
	 */
	List<TransactionLogEntry> getTransactionLogEntries(AccountId owner) throws SubscriptionServiceException;

	/**
	 * Returns all transaction log entries.
	 * @return list of {@link TransactionLogEntry}
	 * @throws SubscriptionServiceException
	 */
	List<TransactionLogEntry> getTransactionLogEntries() throws SubscriptionServiceException;

	/**
	 * Returns all transaction log enties for specific message mask.
	 *
	 * @param messageMask	{@link String} message mask
	 * @return				{@link List} of {@link TransactionLogEntry}
	 * @throws SubscriptionServiceException if any errors occurs
	 */
	List<TransactionLogEntry> getTransactionLogEntriesByMessageMask(String messageMask) throws SubscriptionServiceException;

	/**
	 * Delete all transaction logs.
	 *
	 * @throws SubscriptionServiceException
	 * */
	void deleteTransactionLogs(AccountId accountId) throws SubscriptionServiceException;

	/**
	 * Saves cancellation.
	 *
	 * @param cancellation 	transaction cancellation.
	 * @throws 	   SubscriptionServiceException if error.
	 * */
	void saveCancellation(Cancellation cancellation) throws SubscriptionServiceException;

	/**
	 * Deletes cancellation.
	 *
	 * @param accountId 	user id.
	 * @throws 	   SubscriptionServiceException if error.
	 * */
	void deleteCancellation(String accountId) throws SubscriptionServiceException;

	/**
	 * Gets cancellation.
	 *
	 * @param accountId 	user id.
	 * @throws 	   SubscriptionServiceException if error.
	 * */
	Cancellation getCancellationById(String accountId) throws SubscriptionServiceException;

	/**
	 * Gets all cancellations.
	 *
	 * @throws 	   SubscriptionServiceException if error.
	 * */
	List<Cancellation> getCancellations() throws SubscriptionServiceException;
}
