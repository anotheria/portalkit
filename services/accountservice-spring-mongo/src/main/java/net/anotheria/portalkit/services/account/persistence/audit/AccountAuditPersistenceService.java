package net.anotheria.portalkit.services.account.persistence.audit;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.account.AccountAudit;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * Interface for persistence service for account audit.
 *
 * @author ykalapusha
 */
public interface AccountAuditPersistenceService extends Service {
    /**
     * Saves {@link AccountAudit} instance.
     *
     * @param accountAudit {@link AccountAudit} instance.
     * @throws AccountAuditPersistenceServiceException if any error
     */
    void saveAccountAudit(AccountAudit accountAudit) throws AccountAuditPersistenceServiceException;

    /**
     * Gets {@link List} of {@link AccountAudit} for user.
     *
     * @param accountId     {@link AccountId} of user
     * @return              {@link List} of {@link AccountAudit} for user, or {@code null} if audit disabled
     * @throws AccountAuditPersistenceServiceException if any error
     */
    List<AccountAudit> getAccountAudits(AccountId accountId) throws AccountAuditPersistenceServiceException;
}
