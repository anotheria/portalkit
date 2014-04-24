package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.portalkit.services.accountarchive.persistence.ArchivedAccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.exceptions.PortalKitServiceException;

/**
 * @author VKoulakov
 * @since 21.04.14 18:43
 */
public class AccountArchiveServiceException extends PortalKitServiceException {
    private static final long serialVersionUID = 8508201227826891157L;

    public AccountArchiveServiceException(String message) {
        super(message);
    }

    public AccountArchiveServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountArchiveServiceException(ArchivedAccountPersistenceServiceException e) {
        super("Account archive service operation failed", e);
    }
}
