package net.anotheria.portalkit.services.coin.exception;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * General {@link InsufficientFundsCoinServiceException} exception.
 */
public class InsufficientFundsCoinServiceException extends CoinServiceException {

    private AccountId accountId;
    private int requestedAmount;
    private int availableAmount;

    public InsufficientFundsCoinServiceException(AccountId accountId, int requestedAmount, int availableAmount) {
        super("Insufficient funds");
        this.accountId = accountId;
        this.requestedAmount = requestedAmount;
        this.availableAmount = availableAmount;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }
}
