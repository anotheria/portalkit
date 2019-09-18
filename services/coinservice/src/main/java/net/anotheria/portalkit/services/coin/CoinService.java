package net.anotheria.portalkit.services.coin;

import java.math.BigDecimal;
import java.util.List;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.coin.bean.CoinTransactionBO;
import net.anotheria.portalkit.services.coin.exception.CoinServiceException;
import net.anotheria.portalkit.services.coin.exception.InsufficientFundsCoinServiceException;
import net.anotheria.portalkit.services.common.AccountId;

@DistributeMe(initcode = {
        "net.anotheria.portalkit.services.coin.CoinServiceSpringConfigurator.configure();"
})
@FailBy(strategyClass = RetryCallOnce.class)
public interface CoinService extends Service {

    int getCoinBalance(AccountId accountId) throws CoinServiceException;

    void deposit(AccountId accountId, int amount, String message) throws CoinServiceException;

    boolean canWithdraw(AccountId accountId, int amount, String message) throws CoinServiceException;

    void withdraw(AccountId accountId, int amount, String message) throws InsufficientFundsCoinServiceException, CoinServiceException;

    List<CoinTransactionBO> getTransactions(AccountId accountId) throws CoinServiceException;
}
