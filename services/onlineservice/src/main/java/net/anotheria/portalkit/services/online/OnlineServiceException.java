package net.anotheria.portalkit.services.online;

/**
 * Base exception for {@link OnlineService}.
 *
 * @author h3llka
 */
public class OnlineServiceException extends Exception {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -248132447847526470L;

    /**
     * Constructor.
     *
     * @param message reason message string
     */
    public OnlineServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message reason message string
     * @param cause   {@link Throwable} reason
     */
    public OnlineServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
