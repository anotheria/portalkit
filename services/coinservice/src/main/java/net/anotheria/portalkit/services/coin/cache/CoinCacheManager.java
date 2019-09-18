package net.anotheria.portalkit.services.coin.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.portalkit.services.coin.bean.CoinBalanceBO;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * @author
 */
@Component
public class CoinCacheManager {

    private Cache<AccountId, CoinBalanceBO> coinBalanceCache = Caches.createSoftReferenceExpiringCache("pk-cache-coin-service", 100, 1000, (int) TimeUnit.HOURS.toMillis(2));

    public CoinBalanceBO getCoinBalance(AccountId accountId) {
        return coinBalanceCache.get(accountId);
    }

    public void putCoinBalance(AccountId accountId, CoinBalanceBO coinBalance) {
        coinBalanceCache.put(accountId, coinBalance);
    }

}
