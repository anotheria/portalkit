package net.anotheria.portalkit.services.subscription.persistence.subscription;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.subscription.Cancellation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Lukjanenko
 */
@Service
@Transactional
@Monitor(subsystem = "subscription", category = "portalkit-service")
public class SubscriptionPersistenceServiceImpl implements SubscriptionPersistenceService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveSubscription(SubscriptionDO subscription) throws SubscriptionPersistenceException {

        if (subscriptionExists(subscription.getSubscriptionId())) {
            throw new SubscriptionAlreadyExists(String.valueOf(subscription.getSubscriptionId()));
        }

        entityManager.persist(subscription);
    }

    @Override
    public void updateSubscription(SubscriptionDO subscription) throws SubscriptionPersistenceException {
        entityManager.merge(subscription);
    }

    @Override
    public void saveTransaction(TransactionDO transaction) throws SubscriptionPersistenceException {
        entityManager.persist(transaction);
    }

    @Override
    public void deleteTransactions(String accountId) throws SubscriptionPersistenceException {

        Query query = entityManager.createNamedQuery(TransactionDO.JPQL_DELETE_TRANSACTION)
                .setParameter("accountId", accountId);

        query.executeUpdate();
    }

    @Override
    public void deleteSubscriptions(String accountId) throws SubscriptionPersistenceException {

        Query query = entityManager.createNamedQuery(SubscriptionDO.JPQL_DELETE_SUBSCRIPTION)
                .setParameter("accountId", accountId);

        query.executeUpdate();
    }

    @Override
    public void saveCancellation(Cancellation cancellation) throws SubscriptionPersistenceException {
        entityManager.persist(cancellation.toPersist());
    }

    @Override
    public void deleteCancellation(String accountId) throws SubscriptionPersistenceException {
        Query query = entityManager.createNamedQuery(CancellationDO.JPQL_DELETE_CANCELLATION)
                .setParameter("userId", accountId);

        query.executeUpdate();
    }

    @Override
    public SubscriptionDO getActiveSubscriptionForAccount(String accountId) throws SubscriptionPersistenceException {
        TypedQuery<SubscriptionDO> q = entityManager.createNamedQuery(SubscriptionDO.JPQL_GET_ACTIVE_BY_ACCOUNT_ID, SubscriptionDO.class);
        q.setParameter("accountId", accountId);
        q.setParameter("active", true);

        List<SubscriptionDO> subscriptions = q.getResultList();

        if (subscriptions == null || subscriptions.isEmpty()) {
            throw new SubscriptionPersistenceException("Error occurred while getting active subscription. No subscription found");
        }

        return subscriptions.get(0);
    }

    @Override
    public SubscriptionDO getSubscriptionForAccount(String accountId) throws SubscriptionPersistenceException {
        TypedQuery<SubscriptionDO> q = entityManager.createNamedQuery(SubscriptionDO.JPQL_GET_BY_ACCOUNT_ID, SubscriptionDO.class);
        q.setParameter("accountId", accountId);

        List<SubscriptionDO> subscriptions = q.getResultList();

        if (subscriptions == null || subscriptions.isEmpty()) {
            throw new SubscriptionPersistenceException("Error occurred while getting subscription. No subscription found");
        }

        return subscriptions.get(0);
    }

    @Override
    public Cancellation getCancellationById(String accountId) throws SubscriptionPersistenceException {

        TypedQuery<CancellationDO> q = entityManager.createNamedQuery(CancellationDO.JPQL_GET_BY_ACCOUNT_ID, CancellationDO.class);
        q.setParameter("userId", accountId);

        List<CancellationDO> cancellations = q.getResultList();

        if (cancellations == null || cancellations.isEmpty()) {
            throw new SubscriptionPersistenceException("Error occurred while getting cancellation. No cancellation found");
        }

        return Cancellation.getInstance(cancellations.get(0));
    }

    @Override
    public List<TransactionDO> getTransactionForAccount(String accountId) throws SubscriptionPersistenceException {
        TypedQuery<TransactionDO> q = entityManager.createNamedQuery(TransactionDO.JPQL_GET_BY_ACCOUNT_ID, TransactionDO.class);
        q.setParameter("accountId", accountId);
        return q.getResultList();
    }

    @Override
    public List<TransactionDO> getTransactions() throws SubscriptionPersistenceException {
        TypedQuery<TransactionDO> q = entityManager.createNamedQuery(TransactionDO.JPQL_GET_ALL, TransactionDO.class);
        return q.getResultList();
    }

    @Override
    public long getTransactionsCount() throws SubscriptionPersistenceException {
        TypedQuery<Long> query = entityManager.createQuery("select count(a.id) from TransactionDO a", Long.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<SubscriptionDO> getSubscriptions() throws SubscriptionPersistenceException {
        TypedQuery<SubscriptionDO> q = entityManager.createNamedQuery(SubscriptionDO.JPQL_GET_ALL, SubscriptionDO.class);
        return q.getResultList();
    }

    @Override
    public long getSubscriptionsCount() throws SubscriptionPersistenceException {
        TypedQuery<Long> query = entityManager.createQuery("select count(a.subscriptionId) from SubscriptionDO a", Long.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<Cancellation> getCancellations() throws SubscriptionPersistenceException {

        TypedQuery<CancellationDO> q = entityManager.createNamedQuery(CancellationDO.JPQL_GET_ALL, CancellationDO.class);
        List<Cancellation> cancellations = new ArrayList<>();

        for (CancellationDO cancellation : q.getResultList()) {
            cancellations.add(Cancellation.getInstance(cancellation));
        }

        return cancellations;
    }

    @Override
    public long getCancellationsCount() throws SubscriptionPersistenceException {
        TypedQuery<Long> query = entityManager.createQuery("select count(a.userId) from CancellationDO a", Long.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    private boolean subscriptionExists(long subscriptionId) {

        SubscriptionDO subscription = entityManager.find(SubscriptionDO.class, subscriptionId);

        return subscription != null;
    }
}
