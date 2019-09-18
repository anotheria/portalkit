package net.anotheria.portalkit.services.coin;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.anotheria.portalkit.services.coin.bean.CoinBalanceBO;
import net.anotheria.portalkit.services.coin.bean.CoinTransactionBO;
import net.anotheria.portalkit.services.coin.bean.CoinTransactionType;
import net.anotheria.portalkit.services.coin.bean.mapping.CoinObjectMapper;
import net.anotheria.portalkit.services.coin.cache.CoinCacheManager;
import net.anotheria.portalkit.services.coin.exception.CoinServiceException;
import net.anotheria.portalkit.services.coin.exception.InsufficientFundsCoinServiceException;
import net.anotheria.portalkit.services.coin.persistence.CoinPersistenceService;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinBalanceEntity;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinTransactionEntity;
import net.anotheria.portalkit.services.coin.validation.CoinServiceValidator;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinPersistenceService coinPersistenceService;

    @Autowired
    private CoinCacheManager cacheManager;

    private IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<>();

    @Override
    public int getCoinBalance(AccountId accountId) throws CoinServiceException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            CoinBalanceBO coinBalanceBO = cacheManager.getCoinBalance(accountId);
            if (coinBalanceBO != null) {
                return coinBalanceBO.getAmount();
            }

            CoinBalanceEntity entity = coinPersistenceService.getCoinBalance(accountId.getInternalId());
            if (entity == null) {
                entity = new CoinBalanceEntity();
                entity.setAccountId(accountId.getInternalId());
                entity.setAmount(0);
            }

            cacheManager.putCoinBalance(accountId, CoinObjectMapper.map(entity));

            return entity.getAmount();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deposit(AccountId accountId, int amount, String message) throws CoinServiceException {
        CoinServiceValidator.validateDeposit(accountId, amount);

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            CoinBalanceEntity entity = coinPersistenceService.getCoinBalance(accountId.getInternalId());
            CoinBalanceBO bo;
            if (entity == null) {
                bo = new CoinBalanceBO();
                bo.setAccountId(accountId.getInternalId());
                bo.setAmount(amount);
                entity = CoinObjectMapper.map(bo);
            } else {
                entity.setAmount(entity.getAmount() + amount);
                bo = CoinObjectMapper.map(entity);
            }

            coinPersistenceService.saveCoinBalanceAndTransaction(entity, createCoinTransactionEntity(accountId, amount, message, CoinTransactionType.DEPOSIT));

            cacheManager.putCoinBalance(accountId, bo);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void withdraw(AccountId accountId, int amount, String message) throws InsufficientFundsCoinServiceException, CoinServiceException {
        CoinServiceValidator.validateDeposit(accountId, amount);

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            CoinBalanceEntity entity = coinPersistenceService.getCoinBalance(accountId.getInternalId());
            if (entity == null || entity.getAmount() < amount) {
                throw new InsufficientFundsCoinServiceException(accountId, amount, entity == null ? 0 : entity.getAmount());
            }

            entity.setAmount(entity.getAmount() - amount);
            CoinBalanceBO bo = CoinObjectMapper.map(entity);

            coinPersistenceService.saveCoinBalanceAndTransaction(entity, createCoinTransactionEntity(accountId, amount, message, CoinTransactionType.WITHDRAW));

            cacheManager.putCoinBalance(accountId, bo);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean canWithdraw(AccountId accountId, int amount, String message) throws CoinServiceException {
        CoinServiceValidator.validateWithdraw(accountId, amount);

        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            CoinBalanceEntity entity = coinPersistenceService.getCoinBalance(accountId.getInternalId());
            if (entity == null || entity.getAmount() < amount) {
                return false;
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<CoinTransactionBO> getTransactions(AccountId accountId) throws CoinServiceException {
        return CoinObjectMapper.map(coinPersistenceService.getTransactions(accountId.getInternalId()));
    }

    private CoinTransactionEntity createCoinTransactionEntity(AccountId accountId, int amount, String message, CoinTransactionType type) {
        CoinTransactionEntity ret = new CoinTransactionEntity();

        ret.setId(UUID.randomUUID().toString());
        ret.setAccountId(accountId.getInternalId());
        ret.setAmount(amount);
        ret.setCreated(System.currentTimeMillis());
        ret.setMessage(message);
        ret.setType(type);

        return ret;
    }

}
