package net.anotheria.portalkit.services.accountarchive.persistence;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * @author VKoulakov
 * @since 21.04.14 19:08
 */
public class ArchivedAccountPersistenceServiceException extends PortalKitPersistenceServiceException {

    private static final long serialVersionUID = 366924061201958334L;

    public ArchivedAccountPersistenceServiceException(String message) {
        super(message);
    }

    public ArchivedAccountPersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchivedAccountPersistenceServiceException(Exception e) {
        super(e.getMessage(), e);
    }
}
