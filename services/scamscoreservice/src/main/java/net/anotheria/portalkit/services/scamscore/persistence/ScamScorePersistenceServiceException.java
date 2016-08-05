package net.anotheria.portalkit.services.scamscore.persistence;

/**
 * General {@link ScamScorePersistenceService} exception.
 *
 * @author Vlad Lukjanenko
 */
public class ScamScorePersistenceServiceException extends Exception {

    public ScamScorePersistenceServiceException(String message) {
        super(message);
    }

    public ScamScorePersistenceServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
