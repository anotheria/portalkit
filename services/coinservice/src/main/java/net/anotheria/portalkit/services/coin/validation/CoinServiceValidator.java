package net.anotheria.portalkit.services.coin.validation;

import org.apache.commons.lang3.StringUtils;

import net.anotheria.portalkit.services.coin.exception.CoinServiceValidationException;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * @author
 */
public class CoinServiceValidator {

    public static void validateWithdraw(AccountId accountId, int amount) throws CoinServiceValidationException {
        if (accountId == null || StringUtils.isBlank(accountId.getInternalId())) {
            throw new CoinServiceValidationException("'accountId' should not be empty");
        }
        if (amount < 1) {
            throw new CoinServiceValidationException("'amount' should be positive number");
        }
    }

    public static void validateDeposit(AccountId accountId, int amount) throws CoinServiceValidationException {
        validateWithdraw(accountId, amount);
    }

}
