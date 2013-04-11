package net.anotheria.portalkit.services.online.events;

/**
 * Possible operation types for OnlineActivity stuff.
 *
 * @author h3llka
 */
public enum OnlineActivityESOperation {
    /**
     * Logged in operation.
     */
    ACCOUNT_LOGGED_IN,
    /**
     * Activity time change operation.
     */
    ACCOUNT_ACTIVITY_UPDATE,
    /**
     * Account goes offline.
     */
    ACCOUNT_LOGGED_OUT,
    /**
     * Auto clean up operation.
     */
    AUTO_CLEANUP

}
