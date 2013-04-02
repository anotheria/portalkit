package net.anotheria.portalkit.services.online.persistence;

/**
 * Base exception for {@link ActivityPersistenceService}.
 *
 * @author h3llka
 */
public class ActivityPersistenceServiceException extends Exception {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -3436269506570964856L;

    /**
     * Constructor.
     *
     * @param message reason message
     */
    public ActivityPersistenceServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message reason message
     * @param cause   {@link Throwable} reason error
     */
    public ActivityPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
