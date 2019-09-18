package net.anotheria.portalkit.services.coin.exception;

/**
 * General {@link CoinServiceException} exception.
 */
public class CoinServiceException extends Exception {

    /**
     * Constructor.
     *
     * @param message
     *         error message.
     */
    public CoinServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message
     *         error message.
     * @param cause
     *         exception cause.
     */
    public CoinServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
