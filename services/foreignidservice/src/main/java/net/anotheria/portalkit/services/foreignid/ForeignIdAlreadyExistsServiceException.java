package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;

/**
 * ForeignID service exception class.
 *
 * @author asamoilich
 */
public class ForeignIdAlreadyExistsServiceException extends ForeignIdServiceException {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 2236640256265161980L;

    /**
     * Constructor with message.
     *
     * @param message
     */
    public ForeignIdAlreadyExistsServiceException(String message) {
        super(message);
    }

    /**
     * Constructor with message and {@link Throwable}.
     *
     * @param message
     * @param cause
     */
    public ForeignIdAlreadyExistsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that transform message from persistence exception to the current.
     *
     * @param exception
     */
    public ForeignIdAlreadyExistsServiceException(ForeignIdPersistenceServiceException exception) {
        super("Persistence failure", exception);
    }
}
