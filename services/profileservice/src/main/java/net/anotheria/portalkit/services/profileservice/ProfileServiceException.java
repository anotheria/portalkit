package net.anotheria.portalkit.services.profileservice;

/**
 * @author asamoilich.
 */
public class ProfileServiceException extends Exception {

    /**
     * Generated SerialVersionUID.
     */
    private static final long serialVersionUID = -1718584286520893129L;

    /**
     * Default constructor.
     */
    public ProfileServiceException() {
    }

    /**
     * Public constructor.
     *
     * @param message exception message
     */
    public ProfileServiceException(final String message) {
        super(message);
    }

    /**
     * Public constructor.
     *
     * @param cause exception cause
     */
    public ProfileServiceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Public constructor.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public ProfileServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

