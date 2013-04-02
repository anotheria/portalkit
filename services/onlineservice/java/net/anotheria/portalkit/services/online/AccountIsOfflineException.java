package net.anotheria.portalkit.services.online;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Notifies that during maintenance operation ( logout/ activity time update) - some user was not found among online in users.
 *
 * @author h3llka
 */
public class AccountIsOfflineException extends OnlineServiceException {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -8867854325744267608L;

    /**
     * Constructor.
     *
     * @param account {@link AccountId}
     */
    public AccountIsOfflineException(final AccountId account) {
        super("user with account[" + account + "] was not found among online users.");
    }
}
