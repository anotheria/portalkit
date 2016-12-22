package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammerPersistenceService;

/**
 * General {@link PhotoScammerPersistenceService} exception.
 *
 * @author Vlad Lukjanenko
 */
public class PhotoScammerServiceException extends Exception {

    /**
     * Constructor.
     *
     * @param message   error message.
     * */
    public PhotoScammerServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message   error message.
     * @param cause     exception cause.
     * */
    public PhotoScammerServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
