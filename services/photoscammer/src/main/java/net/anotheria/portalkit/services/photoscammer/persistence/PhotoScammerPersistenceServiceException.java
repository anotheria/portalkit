package net.anotheria.portalkit.services.photoscammer.persistence;

/**
 * General {@link PhotoScammerPersistenceService} exception.
 *
 * @author Vlad Lukjanenko
 */
public class PhotoScammerPersistenceServiceException extends Exception {

    /**
     * Constructor.
     *
     * @param message   error message.
     * */
    public PhotoScammerPersistenceServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message   error message.
     * @param cause     exception cause.
     * */
    public PhotoScammerPersistenceServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
