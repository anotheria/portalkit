package net.anotheria.portalkit.services.common.persistence.jdbc;

/**
 * Base exception for DAO - layer.
 *
 * @author lrosenberg
 * @since 06.01.13 21:37
 */
public class DAOException extends Exception {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 4111902766091391133L;

    /**
     * Constructor.
     *
     * @param message detailed message, description
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause {@link Exception} reason
     */
    public DAOException(Exception cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message detailed message, description
     * @param cause   {@link Exception} reason
     */
    public DAOException(String message, Exception cause) {
        super(message, cause);
    }
}
