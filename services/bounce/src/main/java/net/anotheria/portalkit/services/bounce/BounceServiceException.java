package net.anotheria.portalkit.services.bounce;

/**
 * General {@link BounceServiceException} exception.
 *
 * @author Vlad Lukjanenko
 */
public class BounceServiceException extends Exception {

    /**
     * Constructor.
     *
     * @param message   error message.
     * */
    public BounceServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message   error message.
     * @param cause     exception cause.
     * */
    public BounceServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
