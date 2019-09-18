package net.anotheria.portalkit.services.coin.exception;

/**
 * General {@link CoinServiceValidationException} exception.
 */
public class CoinServiceValidationException extends CoinServiceException {

    /**
     * Constructor.
     *
     * @param message
     *         error message.
     */
    public CoinServiceValidationException(String message) {
        super(message);
    }

}
