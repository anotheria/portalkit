package net.anotheria.portalkit.services.online;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Notifies that some user is already logged in / or  still is online.
 *
 * @author h3llka
 */
public class AccountIsOnlineException extends OnlineServiceException {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -5648293660958106585L;

    /**
     * Constructor.
     *
     * @param account {@link AccountId}
     */
    public AccountIsOnlineException(AccountId account) {
        super("account[" + account + "] is online");
    }
}
