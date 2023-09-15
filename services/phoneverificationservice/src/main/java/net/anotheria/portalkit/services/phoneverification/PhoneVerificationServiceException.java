package net.anotheria.portalkit.services.phoneverification;

/**
 * @author vberkunov.
 */
public class PhoneVerificationServiceException extends Exception {

    /**
     * Default constructor.
     */
    public PhoneVerificationServiceException() {
    }

    /**
     * Public constructor.
     *
     * @param message exception message
     */
    public PhoneVerificationServiceException(final String message) {
        super(message);
    }

    /**
     * Public constructor.
     *
     * @param cause exception cause
     */
    public PhoneVerificationServiceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Public constructor.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public PhoneVerificationServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
