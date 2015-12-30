package net.anotheria.portalkit.services.relation;

/**
 * Common relation service exception.
 *
 * @author asamoilich
 */
public class RelationServiceException extends Exception {
    private static final long serialVersionUID = 2413893204784468116L;

    /**
     * Constructor.
     *
     * @param message fail message
     */
    public RelationServiceException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause fail cause
     */
    public RelationServiceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message fail message
     * @param cause   fail cause
     */
    public RelationServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
