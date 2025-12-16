package net.anotheria.portalkit.services.foreignid;

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
}
