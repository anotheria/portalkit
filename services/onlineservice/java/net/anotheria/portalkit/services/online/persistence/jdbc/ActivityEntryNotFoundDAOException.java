package net.anotheria.portalkit.services.online.persistence.jdbc;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

/**
 * DAOException type - which notifies that Activity entry for selected {@link AccountId} was not found in the persistence.
 *
 * @author h3llka
 */
public class ActivityEntryNotFoundDAOException extends DAOException {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 6196732606818418653L;

    /**
     * Constructor.
     *
     * @param account {@link AccountId}
     */
    public ActivityEntryNotFoundDAOException(AccountId account) {
        super("activityEntry not found for account" + account + "");
    }
}
