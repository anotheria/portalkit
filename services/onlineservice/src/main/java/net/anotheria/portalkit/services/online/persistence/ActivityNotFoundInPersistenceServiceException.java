package net.anotheria.portalkit.services.online.persistence;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Notifies that activity for selected {@link AccountId} was not found in persistence.
 *
 * @author h3llka
 */
public class ActivityNotFoundInPersistenceServiceException extends ActivityPersistenceServiceException {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 6424263239394653941L;

    /**
     * Constructor.
     *
     * @param account {@link AccountId}
     */
    public ActivityNotFoundInPersistenceServiceException(AccountId account) {
        super("No activity for account[" + account + "] found in persistence");
    }
}
