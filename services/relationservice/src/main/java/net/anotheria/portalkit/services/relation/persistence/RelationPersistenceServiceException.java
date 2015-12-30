package net.anotheria.portalkit.services.relation.persistence;

/**
 * Relation persistence common exception.
 *
 * @author asamoilich
 */
public class RelationPersistenceServiceException extends Exception {

    private static final long serialVersionUID = -1752187338783656261L;

    /**
     * Constructor.
     *
     * @param message reason message
     */
    public RelationPersistenceServiceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message reason message
     * @param cause   {@link Throwable} reason error
     */
    public RelationPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
