package net.anotheria.portalkit.services.scamscore;

/**
 * General {@link ScamScoreService} exception.
 *
 * @author Vlad Lukjanenko
 */
public class ScamScoreServiceException extends Exception {

    public ScamScoreServiceException(String message) {
        super(message);
    }

    public ScamScoreServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
