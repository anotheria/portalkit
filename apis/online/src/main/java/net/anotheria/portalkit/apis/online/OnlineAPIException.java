package net.anotheria.portalkit.apis.online;

import net.anotheria.anoplass.api.APIException;

/**
 * Base exception for {@link OnlineAPI}.
 *
 * @author h3llka
 */
public class OnlineAPIException extends APIException {
    /**
     * Basic serial version UID.
     */

    private static final long serialVersionUID = -970358914342928837L;

    /**
     * Constructor.
     *
     * @param message reason message
     */
    public OnlineAPIException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message reason message
     * @param cause   {@link Exception} cause reason
     */
    public OnlineAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
