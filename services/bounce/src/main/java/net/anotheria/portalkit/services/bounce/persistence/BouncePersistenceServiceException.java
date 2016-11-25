package net.anotheria.portalkit.services.bounce.persistence;

/**
 * General {@link BouncePersistenceServiceException} exception.
 *
 * @author Vlad Lukjanenko
 */
public class BouncePersistenceServiceException extends Exception {

    /**
     * Constructor.
     *
     * @param message   error message.
     * */
    public BouncePersistenceServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message   error message.
     * @param cause     exception cause.
     * */
    public BouncePersistenceServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
