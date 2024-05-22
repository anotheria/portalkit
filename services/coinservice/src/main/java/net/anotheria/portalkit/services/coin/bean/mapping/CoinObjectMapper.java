package net.anotheria.portalkit.services.coin.bean.mapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.anotheria.portalkit.services.coin.bean.CoinBalanceBO;
import net.anotheria.portalkit.services.coin.bean.CoinTransactionBO;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinBalanceEntity;
import net.anotheria.portalkit.services.coin.persistence.entity.CoinTransactionEntity;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author
 */
public class CoinObjectMapper {

    public static CoinBalanceBO map(CoinBalanceEntity toMap) {
        CoinBalanceBO ret = new CoinBalanceBO();

        ret.setAccountId(toMap.getAccountId());
        ret.setAmount(toMap.getAmount());

        return ret;
    }

    public static CoinBalanceEntity map(CoinBalanceBO toMap) {
        CoinBalanceEntity ret = new CoinBalanceEntity();

        ret.setAccountId(toMap.getAccountId());
        ret.setAmount(toMap.getAmount());

        return ret;
    }

    public static CoinTransactionBO map(CoinTransactionEntity toMap) {
        CoinTransactionBO ret = new CoinTransactionBO();

        ret.setId(toMap.getId());
        ret.setType(toMap.getType());
        ret.setMessage(toMap.getMessage());
        ret.setCreated(toMap.getCreated());
        ret.setAccountId(toMap.getAccountId());
        ret.setAmount(toMap.getAmount());

        return ret;
    }

    public static CoinTransactionEntity map(CoinTransactionBO toMap) {
        CoinTransactionEntity ret = new CoinTransactionEntity();

        ret.setId(toMap.getId());
        ret.setType(toMap.getType());
        ret.setMessage(toMap.getMessage());
        ret.setCreated(toMap.getCreated());
        ret.setAccountId(toMap.getAccountId());
        ret.setAmount(toMap.getAmount());

        return ret;
    }

    public static List<CoinTransactionBO> map(List<CoinTransactionEntity> toMap) {
        if (CollectionUtils.isEmpty(toMap)) {
            return Collections.emptyList();
        }

        return toMap.stream().map(CoinObjectMapper::map).collect(Collectors.toList());
    }

}
