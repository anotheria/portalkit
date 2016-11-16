package net.anotheria.portalkit.services.personaldata;

/**
 * @author Vlad Lukjanenko.
 */
public class PersonalDataServiceException extends Exception {

    /**
     * Default constructor.
     */
    public PersonalDataServiceException() {
    }

    /**
     * Public constructor.
     *
     * @param message exception message
     */
    public PersonalDataServiceException(final String message) {
        super(message);
    }

    /**
     * Public constructor.
     *
     * @param cause exception cause
     */
    public PersonalDataServiceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Public constructor.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public PersonalDataServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

