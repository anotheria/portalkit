package net.anotheria.portalkit.services.online;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Exception which notifies that LAstLogin/LastActivity data for selected {@link AccountId} was not found....
 * Exactly this  means that such account was not logged in to the system OR {@link OnlineService} failed to persist activity information.
 *
 * @author h3llka
 */
public class NoActivityDataFoundException extends OnlineServiceException {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -8707564850509146302L;

    /**
     * Constructor.
     *
     * @param account {@link AccountId}
     * @param name    requested property name (e.g lastLogin/lastActivity)
     */
    public NoActivityDataFoundException(AccountId account, String name) {
        super("Activity data [" + name + "] for " + account + " was not found");
    }
}
