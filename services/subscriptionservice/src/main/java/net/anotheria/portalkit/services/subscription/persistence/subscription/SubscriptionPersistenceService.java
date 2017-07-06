package net.anotheria.portalkit.services.subscription.persistence.subscription;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.subscription.Cancellation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Monitor
@Service
public interface SubscriptionPersistenceService {

    void saveSubscription(SubscriptionDO subscription) throws SubscriptionPersistenceException;

    void updateSubscription(SubscriptionDO subscription) throws SubscriptionPersistenceException;

    void saveTransaction(TransactionDO transaction) throws SubscriptionPersistenceException;

    void deleteTransactions(String accountId) throws SubscriptionPersistenceException;

    void deleteSubscriptions(String accountId) throws SubscriptionPersistenceException;

    void saveCancellation(Cancellation cancellation) throws SubscriptionPersistenceException;

    void deleteCancellation(String accountId) throws SubscriptionPersistenceException;

    SubscriptionDO getActiveSubscriptionForAccount(String accountId) throws SubscriptionPersistenceException;

    SubscriptionDO getSubscriptionForAccount(String accountId) throws SubscriptionPersistenceException;

    Cancellation getCancellationById(String accountId) throws SubscriptionPersistenceException;

    List<TransactionDO> getTransactionForAccount(String accountId) throws SubscriptionPersistenceException;

    List<TransactionDO> getTransactions() throws SubscriptionPersistenceException;

    List<SubscriptionDO> getSubscriptions() throws SubscriptionPersistenceException;

    List<Cancellation> getCancellations() throws SubscriptionPersistenceException;
}
