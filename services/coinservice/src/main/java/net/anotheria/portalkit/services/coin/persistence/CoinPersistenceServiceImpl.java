package net.anotheria.portalkit.services.coin.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinBalanceEntity;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinTransactionEntity;

@Monitor(subsystem = "bounce")
@Service
public class CoinPersistenceServiceImpl implements CoinPersistenceService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveCoinTransaction(CoinTransactionEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return;
        }

        entityManager.merge(entity);
    }

    @Override
    public void saveCoinBalance(CoinBalanceEntity entity) {
        entityManager.merge(entity);
    }

    @Override
    public void saveCoinBalanceAndTransaction(CoinBalanceEntity balance, CoinTransactionEntity transaction) {
        saveCoinBalance(balance);
        saveCoinTransaction(transaction);
    }

    @Override
    public CoinBalanceEntity getCoinBalance(String accountId) {
        try {
            return entityManager.createQuery("select e from CoinBalanceEntity e where e.accountId = :accountId", CoinBalanceEntity.class).setParameter("accountId", accountId).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<CoinTransactionEntity> getTransactions(String accountId) {
        return entityManager.createQuery("select e from CoinTransactionEntity e where e.accountId = :accountId order by e.created desc", CoinTransactionEntity.class)
                .setParameter("accountId", accountId).getResultList();
    }
}
