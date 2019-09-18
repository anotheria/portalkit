package net.anotheria.portalkit.services.coin.persistence;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinBalanceEntity;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinTransactionEntity;

@Monitor
@Service
public interface CoinPersistenceService {

    @Transactional
    void saveCoinTransaction(CoinTransactionEntity entity);

    @Transactional
    void saveCoinBalance(CoinBalanceEntity entity);

    @Transactional
    void saveCoinBalanceAndTransaction(CoinBalanceEntity balance, CoinTransactionEntity transaction);

    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    CoinBalanceEntity getCoinBalance(String accountId);

    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    List<CoinTransactionEntity> getTransactions(String accountId);
}
